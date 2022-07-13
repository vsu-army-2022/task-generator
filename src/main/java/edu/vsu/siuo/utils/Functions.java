package edu.vsu.siuo.utils;

import edu.vsu.siuo.domains.*;
import edu.vsu.siuo.domains.dto.ShotDto;
import edu.vsu.siuo.domains.enums.Direction;
import edu.vsu.siuo.domains.enums.Powers;
import edu.vsu.siuo.domains.enums.Types;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static edu.vsu.siuo.utils.Utils.rand;

public class Functions {

    public static double converseToRad(double a) {
        return a * Math.PI / 30 / 100;
    }

    public static int converseToDelAngle(double rad) {
        return (int) (Math.round(rad * 30 / Math.PI * 100) % 6000);
    }

    /**
     * Перевод угла в делениях дальномера в строку вида +00-15
     *
     * @param a угол в делениях угломера
     */
    public static String angDash(double a) {
        int c = ((int) Math.abs(Math.round(a))) / 100;
        int d = (int) Math.abs(Math.round(a)) % 100;
        String dd = (d < 10 ? "0" + d : Integer.toString(d));

        String result = (a < 0 ? "-" : "+") + c + "-" + dd;

        // fixme?
        if (result.equals("+0-00")) {
            result = "";
        }

        return result;
    }

    public static String modAngDash(double a) {
        int c = Math.abs((int) Math.round(a)) / 100;
        int d = (int) Math.abs(Math.round(a)) % 100;
        return c + "-" + (d < 10 ? "0" + d : d);
    }


    public static double[] findDalnUgol(double x1, double y1, double x2, double y2) {
        // катеты ОП - цели
        double kat_x = x1 - x2;
        double kat_y = y1 - y2;

        // дальность топографическая
        double dt = Math.sqrt(kat_x * kat_x + kat_y * kat_y);

//        // доворот, пс
//        if (kat_x == 0) kat_x = 0.0000000001; // скрываем ошибку (на ноль делить нельзя)
//        double angleFromONtoTarget = Math.atan(Math.abs(kat_y / kat_x));
        double angleFromONtoTarget; // угол между ОП с основным направлением стрельбы и целью
        if (kat_x == 0) {
            angleFromONtoTarget = Math.toRadians(Math.PI / 2);
        } else {
            angleFromONtoTarget = Math.atan(Math.abs(kat_y / kat_x));
        }

        double a;

        if (kat_x > 0 && kat_y > 0) {
            a = angleFromONtoTarget;
        } else if (kat_x < 0 && kat_y > 0) {
            a = Math.PI - angleFromONtoTarget;
        } else if (kat_x < 0 && kat_y < 0) {
            a = Math.PI + angleFromONtoTarget;
        } else {
            a = 2 * Math.PI - angleFromONtoTarget;
        }

        double a_du = converseToDelAngle(a);

        return new double[]{dt, a_du}; //дальность топографическая между двумя точками, a_du угол (в делениях угломера) между основным направлением и целью
    }

    public static AnalysisResult analyzePuo(Target target, ObjectPosition knp, Double np_x, Double np_y, OP op) {

        AnalysisResult analysisResult = new AnalysisResult();

        // перевод из дел.угломера в радианы
        double angleFromKNPtoTargetRadians = converseToRad(target.getAngleFromKNPtoTarget());

        // катеты КНП - Цель
        double kat_knp_x = target.getDistanceFromKNPtoTarget() * Math.cos(angleFromKNPtoTargetRadians); // расстояние по X между КНП и целью
        double kat_knp_y = target.getDistanceFromKNPtoTarget() * Math.sin(angleFromKNPtoTargetRadians);// расстояние по Y между КНП и целью

        // координаты X и Y цели
        double cel_x = knp.getX() + kat_knp_x;
        double cel_y = knp.getY() + kat_knp_y;
        analysisResult.setCelX(cel_x);
        analysisResult.setCelY(cel_y);

        double[] daln_ugol = findDalnUgol(cel_x, cel_y, op.getX(), op.getY()); //расстояние и угол (в делениях угломера) между ОП и целью
        double distanceFromOPtoTarget = daln_ugol[0];
        double angleFromOPtoTarget = daln_ugol[1];
        analysisResult.setDalTop(distanceFromOPtoTarget);

        double ps = Math.abs(angleFromOPtoTarget - target.getAngleFromKNPtoTarget());
        analysisResult.setPs(ps);

        double dov_top = angleFromOPtoTarget - op.getMainDirection();
        analysisResult.setDovTop(dov_top);

        // ОП Слева или Справа ?
        Direction opDirection = angleFromOPtoTarget > target.getAngleFromKNPtoTarget() ? Direction.LEFT : Direction.RIGHT;
        analysisResult.setOpDirection(opDirection);


        // сопряженка
        if (np_x != null && np_y != null) {
            daln_ugol = findDalnUgol(cel_x, cel_y, np_x, np_y);
            double dal_np = daln_ugol[0];
            double a_np_op_du = daln_ugol[1];

            double alfa_np = Math.abs(a_np_op_du - angleFromOPtoTarget);
            analysisResult.setAlfaNp(alfa_np);

            double gamma = Math.abs(a_np_op_du - target.getAngleFromKNPtoTarget());
            analysisResult.setGamma(gamma);


            if (target.getAngleFromKNPtoTarget() > a_np_op_du) {
                analysisResult.setDpL(target.getDistanceFromKNPtoTarget());
                analysisResult.setApL(ps);
                analysisResult.setDpR(dal_np);
                analysisResult.setApR(alfa_np);
            } else {
                analysisResult.setDpL(dal_np);
                analysisResult.setApL(alfa_np);
                analysisResult.setDpR(target.getDistanceFromKNPtoTarget());
                analysisResult.setApR(ps);
            }


            // Позиция ОП при сопряженке
            if ((target.getAngleFromKNPtoTarget() < angleFromOPtoTarget && angleFromOPtoTarget < a_np_op_du) || (target.getAngleFromKNPtoTarget() > angleFromOPtoTarget && angleFromOPtoTarget > a_np_op_du)) {
                analysisResult.setOpDirSopryzh(Direction.MIDDLE);
            } else {
                analysisResult.setOpDirSopryzh(opDirection);
            }
        }

        return analysisResult;
    }

    public static String vPricel(double d) {
        if (d > 0) {
            return "+" + (int) Math.round(d);
        }
        // fixme сравнение с eps?
        // if d == 0
        return "";
    }

    public static String formatNabl(Integer a, String h, Integer f) {
        String as = String.valueOf(a);
        if (a != null) {
            as = (a < 0 ? "Л" : "П") + Math.abs(a) + ", ";
        }
        String fs;
        if (f != null) {
            fs = ", Фр. " + modAngDash(f);
        } else {
            fs = "";
        }
        return as + h + fs;
    }

    public static Double cosec(double value) {
        return 1.0 / Math.sin(value);
    }

    public static Double sec(double value) {
        return 1.0 / Math.cos(value);
    }

    public static Double cotan(double value) {
        return Math.cos(value) / Math.sin(value);
    }

    public static String formatNablDalnomer(int a, Types h, double d, double ak, double dk) {
        String as = modAngDash(ak + a); //fixme?
        String hs = h.getDescription();
        if (h.equals(Types.ONE_P)) hs = Double.toString(dk + d);
        if (h.equals(Types.ONE_N)) hs = Double.toString(dk - d);
        return as + ", " + hs;
    }

    public static String formatNablSn(Double al, Double ap) {
        if (al != null) al = (al < 0 ? 'Л' : 'П') + Math.abs(al);
        if (ap != null) ap = (ap < 0 ? 'Л' : 'П') + Math.abs(ap);
        return al + ", " + ap;
    }

    /**
     * Расчет графика расчета поправок (ГРП)
     *
     * @param grp  Опорные топографические дальности метеорологических поправок (D - дальность, dD - поправка, dd - доворот)
     * @param strD Дальность до цели
     * @return Список, содержащий дальность [0] и доворот [1] исчисленные
     */
    public static List<Double> grpCount(GRP grp, double strD) {
        //Вычисление исчисленных опорных дальностей


//        // create deep copy of map
//        Map<Integer, Map<String, Integer>> grpCopy = new HashMap<>();
//
//        for (int key : grp.keySet()) {
//            grpCopy.put(key, new HashMap<>());
//            for (String key2 : grp.get(key).keySet()) {
//                int value2 = grp.get(key).get(key2);
//                grpCopy.get(key).put(key2, value2);
//            }
//        }
        grp.setDistance_1(grp.getDistance_1() - grp.getDifDistance_1());
        grp.setDistance_2(grp.getDistance_2() - grp.getDifDistance_2());
        grp.setDistance_3(grp.getDistance_3() - grp.getDifDistance_3());

//
//        grpCopy.forEach((key, value) -> grpCopy.get(key).replace("D", grpCopy.get(key).get("D") - grpCopy.get(key).get("dD")));


        Map<String, Integer> left = new HashMap<>();
        Map<String, Integer> right = new HashMap<>();

        // Вычисление промежутка, в котором находится цель, между двумя опорными дальностями

        if (strD < grp.getDistance_2()) {
            left.put("D", grp.getDistance_1());
            left.put("dD", grp.getDifDistance_1());
            left.put("dd", grp.getTurn_1());
            right.put("D", grp.getDistance_2());
            right.put("dD", grp.getDifDistance_2());
            right.put("dd", grp.getTurn_2());
        } else {
            left.put("D", grp.getDistance_2());
            left.put("dD", grp.getDifDistance_2());
            left.put("dd", grp.getTurn_2());
            right.put("D", grp.getDistance_3());
            right.put("dD", grp.getDifDistance_3());
            right.put("dd", grp.getTurn_3());
        }

//        if (strD < grpCopy.get(1).get("D")) {
//            left.putAll(grpCopy.get(0));
//            right.putAll(grpCopy.get(1));
//        } else {
//            left.putAll(grpCopy.get(1));
//            right.putAll(grpCopy.get(2));
//        }

        // Расчет исчисленных дальности и доворота до цели
        List<Double> ret = Stream
                .of("dD", "dd")
                .map(_grp -> left.get(_grp) + (right.get(_grp) - left.get(_grp)) * (strD - left.get("D")) / (right.get("D") - left.get("D")))
                .collect(Collectors.toList());

        // Округление значений
        IntStream.range(0, ret.size() - 1).forEach(i -> ret.set(i, (double) Math.round(ret.get(i))));

        return ret;
    }

    public static HashMap<Powers, ArrayList<ArrayList<Double>>> getTS() {
        Powers type = null;
        HashMap<Powers, ArrayList<ArrayList<Double>>> ts = new HashMap<>();
        ts.put(Powers.Full, new ArrayList<>());
        ts.put(Powers.Reduced, new ArrayList<>());
        ts.put(Powers.Power1, new ArrayList<>());
        ts.put(Powers.Power2, new ArrayList<>());
        ts.put(Powers.Power3, new ArrayList<>());
        ts.put(Powers.Power4, new ArrayList<>());
        try (FileReader fr = new FileReader("src/main/resources/edu/vsu/siuo/ts.txt")) {
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                if (line.contains("Полный")) {
                    type = Powers.Full;
                } else if (line.contains("Уменьшенный")) {
                    type = Powers.Reduced;
                } else if (line.contains("Первый")) {
                    type = Powers.Power1;
                } else if (line.contains("Второй")) {
                    type = Powers.Power2;
                } else if (line.contains("Третий")) {
                    type = Powers.Power3;
                } else if (line.contains("Четвертый")) {
                    type = Powers.Power4;
                } else if (line.split("\t").length == 4 && !line.contains("Дальн")) {
                    String[] line_ts = line.split("\t");

                    ArrayList<Double> list_ts = new ArrayList<>();
                    list_ts.add(Double.parseDouble(line_ts[0]));
                    list_ts.add(Double.parseDouble(line_ts[1]));
                    list_ts.add(Double.parseDouble(line_ts[2]));
                    list_ts.add(Double.parseDouble(line_ts[3]));

                    ts.get(type).add(list_ts);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ts;
    }

    public static ArrayList<Double> ts(Powers power, double distance) {
        HashMap<Powers, ArrayList<ArrayList<Double>>> ts = getTS();
        ArrayList<Double> result = new ArrayList<>();
        result.add(distance);
        double closelyDistance;
        double remainder;

        if (distance % 200 > 0 && distance > 200) {
            if (distance % 200 > 200 - distance % 200) {
                remainder = -(200 - distance % 200);
            } else {
                remainder = distance % 200;
            }
            closelyDistance = distance - remainder;
            int closelyIndex = (int) Math.round((closelyDistance - 200) / 200);
            ArrayList<Double> closelyValue = ts.get(power).get(closelyIndex);
            int nextIndex;
            nextIndex = (remainder > 0 && closelyIndex + 1 < ts.get(power).size() ? closelyIndex + 1 : closelyIndex - 1);
            nextIndex = (nextIndex < 0 ? closelyIndex : nextIndex);
            ArrayList<Double> nextValue = ts.get(power).get(nextIndex);
            double distanceDiffernce = Math.abs(closelyValue.get(0) - nextValue.get(0));
            result.add(closelyValue.get(1) + Math.abs(closelyValue.get(1) - nextValue.get(1)) / distanceDiffernce * remainder);
            result.add(closelyValue.get(2) + Math.abs(closelyValue.get(2) - nextValue.get(2)) / distanceDiffernce * remainder);
            result.add(closelyValue.get(3) + Math.abs(closelyValue.get(3) - nextValue.get(3)) / distanceDiffernce * remainder);
            return result;
        } else {
            return ts.get(power).get((int) Math.round((distance - 200) / 200));
        }
    }


    public static GeneratedShotResult generateShot(Target target) {

        List<Types> gen_n = Types.getNoEmptyTypes();

        // генерируем наблюдения
        Map<Integer, ShotDto> shot = new HashMap<>();

        shot.put(0, new ShotDto());
        shot.put(1, new ShotDto());
        shot.put(2, new ShotDto());

        if (target.getTargetsDepth() < 100) {
            shot.get(0).setA((rand(0, 1) == 1 ? 1 : -1) * rand(60, 95));
            shot.get(0).setType(gen_n.get(rand(1, 2)));

            shot.get(1).setA((rand(0, 1) == 1 ? 1 : -1) * rand(30, 55));
            shot.get(1).setType(shot.get(0).getType() == Types.ONE_N ? Types.ONE_P : Types.ONE_N);

            shot.get(2).setA((rand(0, 1) == 1 ? 1 : -1) * rand(3, 26));
            shot.get(2).setType(gen_n.get(rand(1, 2)));
        } else {
            shot.get(0).setA((rand(0, 1) == 1 ? 1 : -1) * rand(60, 95));
            shot.get(0).setType(gen_n.get(rand(0, 2)));

            shot.get(1).setA((rand(0, 1) == 1 ? 1 : -1) * rand(30, 55));
            shot.get(2).setA((rand(0, 1) == 1 ? 1 : -1) * rand(3, 26));

            if (shot.get(0).getType() == Types.XZ) {
                shot.get(1).setType(gen_n.get(rand(1, 2)));
                shot.get(2).setType((shot.get(1).getType() == Types.ONE_N ? Types.ONE_P : Types.ONE_N));
            } else {
                shot.get(1).setType(shot.get(0).getType() == Types.ONE_P ? Types.ONE_P : Types.ONE_N);
                shot.get(2).setType((shot.get(1).getType() == Types.ONE_N ? Types.ONE_P : Types.ONE_N));
            }
        }

        shot.put(3, new ShotDto());
        shot.get(3).setA((rand(0, 1) == 1 ? 1 : -1) * rand(5, 21));
        shot.get(3).setType(gen_n.get(rand(3, 4)));

        shot.put(4, new ShotDto());

        if (target.getTargetsFrontDu() == 0) {
            shot.get(3).setF(rand(Math.round(14 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(28 * 1000 / target.getDistanceFromKNPtoTarget()))); // rand(round(40*1000/distanceFromKNPtoTarget),round(280*1000/distanceFromKNPtoTarget));
            shot.get(4).setF(rand(Math.round(14 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(28 * 1000 / target.getDistanceFromKNPtoTarget())));
        } else {
            if (target.getTargetsFrontDu() < 120) {
                shot.get(3).setF((int) Math.round(target.getTargetsFrontDu() + rand(Math.round(90 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(120 * 1000 / target.getDistanceFromKNPtoTarget()))));
            } else {
                shot.get(3).setF((int) Math.round(target.getTargetsFrontDu() + rand(Math.round(140 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(190 * 1000 / target.getDistanceFromKNPtoTarget()))));
            }
            shot.get(4).setF((int) Math.round(target.getTargetsFrontDu() + (rand(0, 1) == 1 ? 1 : -1) * rand(Math.round(6 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(28 * 1000 / target.getDistanceFromKNPtoTarget()))));
        }

        if (target.getTargetsDepth() < 100) {
            shot.get(4).setType(gen_n.get(rand(5, 6)));
        } else {
            shot.get(4).setType(gen_n.get(rand(7, 8)));
        }
        shot.get(4).setA((rand(0, 1) == 1 ? 1 : -1) * rand(2, 16));
        return new GeneratedShotResult(shot);
    }

    public static GeneratedShotResult generateShotDalnomer(Target target) {

        List<Types> gen_n = Types.getNoEmptyTypes();

        // генерируем наблюдения
        Map<Integer, ShotDto> shot = new HashMap<>();

        shot.put(0, new ShotDto());
        shot.put(1, new ShotDto());
        shot.put(2, new ShotDto());

        shot.get(0).setA((rand(0, 1) == 1 ? 1 : -1) * rand(40, 65));
        shot.get(0).setType(gen_n.get(rand(0, 2)));

        if (shot.get(0).getType() == Types.XZ) {
            shot.get(0).setRazr(0);
        } else {
            shot.get(0).setRazr(rand(150, 250));
        }

            /* 3 выстрела */
            int v3snar1A = (rand(0, 1) == 1 ? 1 : -1) * rand(20, 38);
            int v3snar1D = rand(50, 120);
            int v3snar0A = v3snar1A - rand(4, 11);
            int v3snar0D = v3snar1D - rand(18, 32);
            int v3snar2A = v3snar1A + rand(4, 11);
            int v3snar2D = v3snar1D + rand(18, 32);


        shot.get(1).setA(Math.round((v3snar0A + v3snar1A + v3snar2A) / 3));
        shot.get(1).setType(gen_n.get(rand(1, 2)));
        shot.get(1).setRazr(Math.round((v3snar0D + v3snar1D + v3snar2D) / 3));

        String vse3v = "";
        String vse3v_word = "";

        // extracted for loop
        vse3v = vse3v + '	' + formatNablDalnomer(v3snar0A, shot.get(1).getType(), v3snar0D, target.getAngleFromKNPtoTarget(), target.getDistanceFromKNPtoTarget()) + '	';
        vse3v_word = vse3v_word + formatNablDalnomer(v3snar0A, shot.get(1).getType(), v3snar0D, target.getAngleFromKNPtoTarget(), target.getDistanceFromKNPtoTarget()) + "; ";

        vse3v = vse3v + '	' + formatNablDalnomer(v3snar1A, shot.get(1).getType(), v3snar1D, target.getAngleFromKNPtoTarget(), target.getDistanceFromKNPtoTarget()) + '	';
        vse3v_word = vse3v_word + formatNablDalnomer(v3snar1A, shot.get(1).getType(), v3snar1D, target.getAngleFromKNPtoTarget(), target.getDistanceFromKNPtoTarget()) + "; ";

        vse3v = vse3v + '	' + formatNablDalnomer(v3snar2A, shot.get(1).getType(), v3snar2D, target.getAngleFromKNPtoTarget(), target.getDistanceFromKNPtoTarget()) + '	';
        vse3v_word = vse3v_word + formatNablDalnomer(v3snar2A, shot.get(1).getType(), v3snar2D, target.getAngleFromKNPtoTarget(), target.getDistanceFromKNPtoTarget()) + "; ";


        // на поражение
        shot.get(3).setA((rand(0, 1) == 1 ? 1 : -1) * rand(5, 21));
        shot.get(2).setType(gen_n.get(rand(3, 4)));

        if (target.getAngularMagnitudeTarget() == 0) {
            shot.get(2).setF(rand(Math.round(14 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(28 * 1000 / target.getDistanceFromKNPtoTarget())));
            shot.get(3).setF(rand(Math.round(14 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(28 * 1000 / target.getDistanceFromKNPtoTarget())));
        } else {
            if (target.getAngularMagnitudeTarget() < 120) {
                shot.get(2).setF(target.getAngularMagnitudeTarget() + rand(Math.round(90 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(120 * 1000 / target.getDistanceFromKNPtoTarget())));
            } else {
                shot.get(2).setF(target.getAngularMagnitudeTarget() + rand(Math.round(140 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(190 * 1000 / target.getDistanceFromKNPtoTarget())));
            }
            shot.get(3).setF(target.getAngularMagnitudeTarget() + (rand(0, 1) == 1 ? 1 : -1) * rand(Math.round(6 * 1000 / target.getDistanceFromKNPtoTarget()), Math.round(28 * 1000 / target.getDistanceFromKNPtoTarget())));
        }


        if (target.getTargetsDepth() < 100) {
            shot.get(3).setType(gen_n.get(rand(5, 6)));
        } else {
            shot.get(3).setType(gen_n.get(rand(7, 8)));
        }
        shot.get(3).setA((rand(0, 1) == 1 ? 1 : -1) * rand(2, 16));

        return new GeneratedShotResult(shot, vse3v, vse3v_word);
    }
}
