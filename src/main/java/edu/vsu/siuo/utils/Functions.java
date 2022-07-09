package edu.vsu.siuo.utils;

import edu.vsu.siuo.domains.AnalysisResult;
import edu.vsu.siuo.domains.OP;
import edu.vsu.siuo.domains.ObjectPosition;
import edu.vsu.siuo.domains.Target;
import edu.vsu.siuo.domains.enums.Powers;

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

public class Functions {

    public static double converseToRad(double a) {
        return a * Math.PI / 30 / 100;
    }

    public static int converseToDelAngle(double rad) {
        return (int) (Math.round(rad * 30 / Math.PI * 100) % 6000);
    }

    public static String angDash(double a) {
        int c = ((int) Math.abs(a)) / 100;
        int d = (int) Math.abs(a) % 100;
        String dd = (d < 10 ? "0" + d : Integer.toString(d));

        String result = (a < 0 ? "-" : "+") + c + "-" + dd;

        if (result.equals("+0-00")) {
            result = "";
        }

        return result;
    }

    public static String modAngDash(double a) {
        int c = Math.abs((int) a) / 100;
        int d = (int) Math.abs(a) % 100;
        return c + "-" + (d < 10 ? "0" + d : d);
    }


    public static double[] findDalnUgol(double x1, double y1, double x2, double y2) {
        // катеты ОП - цели
        double kat_x = x1 - x2;
        double kat_y = y1 - y2;

        // дальность топографическая
        double dt = Math.sqrt(kat_x * kat_x + kat_y * kat_y);
        int dal = (int) dt;

//        // доворот, пс
//        if (kat_x == 0) kat_x = 0.0000000001; // скрываем ошибку (на ноль делить нельзя)
//        double angleFromONtoTarget = Math.atan(Math.abs(kat_y / kat_x));
        double angleFromONtoTarget; // угол между ОП с основным направлением стрельбы и целью
        if (kat_x == 0) {
            angleFromONtoTarget = Math.toRadians(Math.PI/2);
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

        return new double[]{dal, a_du}; //дальность топографическая между двумя точками, a_du угол (в делениях угломера) между основным направлением и целью
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
        String op_dir = angleFromOPtoTarget > target.getAngleFromKNPtoTarget() ? "l" : "r";
        analysisResult.setOpDir(op_dir);


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
                analysisResult.setOpDirSopryzh("Посередине");
            } else {
                analysisResult.setOpDirSopryzh(op_dir.equals("l") ? "Слева" : "Справа");
            }
        }

        return analysisResult;
    }

    public static String vPricel(double d) {
        if (d > 0) {
            return "+" + (int) d;
        }
        // fixme сравнение с eps?
        // if d == 0
        return "";
    }

    public static String formatNabl(Integer a, String h, Integer f, Map<String, String> TYPES) {
        String as = String.valueOf(a);
        if (a != null) {
            as = (a < 0 ? "Л" : "П") + Math.abs(a) + ", ";
        }
        String fs = String.valueOf(f);
        if (f != null) {
            fs = ", Фр. " + modAngDash(f);
        } else {
            fs = "";
        }
        return as + TYPES.get(h) + fs;
    }

    public static String formatNablDalnomer(String a, String h, double d, double ak, double dk) {
        if (!a.equals("")) a = modAngDash(ak + Double.parseDouble(a));
        if (h.equals("xz")) h = "«?»";
        if (h.equals("one_p")) h = Double.toString(dk + d);
        if (h.equals("one_n")) h = Double.toString(dk - d);
        return a + ", " + h;
    }

    public static String formatNablSn(Double al, Double ap) {
        if (al != null) al = (al < 0 ? 'Л' : 'П') + Math.abs(al);
        if (ap != null) ap = (ap < 0 ? 'Л' : 'П') + Math.abs(ap);
        return al + ", " + ap;
    }

    public static List<Double> grpCount(Map<Integer, Map<String, Integer>> d, double strD) {
        d.forEach((key, value) -> d.get(key).replace("D", d.get(key).get("D") - d.get(key).get("dD")));

        Map<String, Integer> left = new HashMap<>();
        Map<String, Integer> right = new HashMap<>();

        if (strD < d.get(1).get("D")) {
            left.putAll(d.get(0));
            right.putAll(d.get(1));
        } else {
            left.putAll(d.get(1));
            right.putAll(d.get(2));
        }

        List<Double> ret = Stream
                .of("dD", "dd")
                .map(grp -> left.get(grp) + (right.get(grp) - left.get(grp)) * (strD - left.get("D")) / (right.get("D") - left.get("D")))
                .collect(Collectors.toList());

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
        try (FileReader fr = new FileReader("src\\main\\resources\\edu\\vsu\\siuo\\ts.txt")) {
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
            int closelyIndex = (int) (closelyDistance - 200) / 200;
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
            return ts.get(power).get((int) (distance - 200) / 200);
        }
    }
}
