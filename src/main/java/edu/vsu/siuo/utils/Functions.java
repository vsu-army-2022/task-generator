package edu.vsu.siuo.utils;

import edu.vsu.siuo.domains.AnalysisResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // доворот, пс
        if (kat_x == 0) kat_x = 0.0000000001; // скрываем ошибку (на ноль делить нельзя)
        double rumb = Math.atan(Math.abs(kat_y / kat_x));

        double a;

        if (kat_x > 0 && kat_y > 0) {
            a = rumb;
        } else if (kat_x < 0 && kat_y > 0) {
            a = Math.PI - rumb;
        } else if (kat_x < 0 && kat_y < 0) {
            a = Math.PI + rumb;
        } else {
            a = 2 * Math.PI - rumb;
        }

        double a_du = converseToDelAngle(a);

        return new double[]{dal, a_du};
    }

    public static AnalysisResult analyzePuo(double dk, double ak, double knp_x, double knp_y, Double np_x, Double np_y, double op_x, double op_y, double on) {

        AnalysisResult analysisResult = new AnalysisResult();

        // перевод из дел.угломера в радианы
        double ugol_rad = converseToRad(ak);

        // катеты КНП - Цель
        double kat_knp_x = dk * Math.cos(ugol_rad);
        double kat_knp_y = dk * Math.sin(ugol_rad);

        // координаты X и Y цели
        double cel_x = knp_x + kat_knp_x;
        analysisResult.setCelX(cel_x);
        double cel_y = knp_y + kat_knp_y;
        analysisResult.setCelY(cel_y);

        double[] daln_ugol = findDalnUgol(cel_x, cel_y, op_x, op_y);
        double dal_top = daln_ugol[0];
        analysisResult.setDalTop(dal_top);
        double a_knp_op_du = daln_ugol[1];

        double ps = Math.abs(a_knp_op_du - ak);
        analysisResult.setPs(ps);

        double dov_top = a_knp_op_du - on;
        analysisResult.setDovTop(dov_top);

        // ОП Слева или Справа ?
        String op_dir = a_knp_op_du > ak ? "l" : "r";
        analysisResult.setOpDir(op_dir);


        // сопряженка
        if (np_x != null && np_y != null) {
            daln_ugol = findDalnUgol(cel_x, cel_y, np_x, np_y);
            double dal_np = daln_ugol[0];
            double a_np_op_du = daln_ugol[1];

            double alfa_np = Math.abs(a_np_op_du - a_knp_op_du);
            analysisResult.setAlfaNp(alfa_np);

            double gamma = Math.abs(a_np_op_du - ak);
            analysisResult.setGamma(gamma);


            if (ak > a_np_op_du) {
                analysisResult.setDpL(dk);
                analysisResult.setApL(ps);
                analysisResult.setDpR(dal_np);
                analysisResult.setApR(alfa_np);
            } else {
                analysisResult.setDpL(dal_np);
                analysisResult.setApL(alfa_np);
                analysisResult.setDpR(dk);
                analysisResult.setApR(ps);
            }


            // Позиция ОП при сопряженке
            if ((ak < a_knp_op_du && a_knp_op_du < a_np_op_du) || (ak > a_knp_op_du && a_knp_op_du > a_np_op_du)) {
                analysisResult.setOpDirSopryzh("Посередине");
            } else {
                analysisResult.setOpDirSopryzh(op_dir.equals("l") ? "Слева" : "Справа");
            }
        }

        return analysisResult;
    }

    public static String vPricel(double d) {
        if (d > 0) {
            return "+" + d;
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
//        IntStream.range(0, 2).forEach(i -> d.get("D").put(i, d.get("D").get(i) - d.get("dD").get(i)));
//
//        Map<String, Double> left = new HashMap<>();
//        Map<String, Double> right = new HashMap<>();
//        if (strD < d.get("D").get(1)) {
//            sidePut(d, left, 0);
//            sidePut(d, right, 1);
//        } else {
//            sidePut(d, left, 1);
//            sidePut(d, right, 2);
//        }
//
//        Map<String, Double> ret = new HashMap<>();
//        d.keySet().stream().filter(grp -> !grp.equals("D")).forEach(grp -> {
//            double tmp = left.get(grp) +
//                         (right.get(grp) - left.get(grp)) *
//                         (strD - left.get("D")) /
//                         (right.get("D") - left.get("D"));
//            ret.put(grp, tmp);
//        });
//
//        ret.replaceAll((g, v) -> (double) Math.round(ret.get(g)));
//        return ret;
        return List.of();
    }

    private static void sidePut(Map<String, Map<Integer, Double>> d, Map<String, Double> side, int i) {
        d.keySet().forEach(grp -> side.put(grp, d.get(grp).get(i)));
    }

    public static HashMap<String, ArrayList<ArrayList<Double>>> getTS() {
        String type = null;
        HashMap<String, ArrayList<ArrayList<Double>>> ts = new HashMap<>();
        ts.put("Полный", new ArrayList<>());
        ts.put("Уменьшенный", new ArrayList<>());
        ts.put("Первый", new ArrayList<>());
        ts.put("Второй", new ArrayList<>());
        ts.put("Третий", new ArrayList<>());
        ts.put("Четвертый", new ArrayList<>());
        try (FileReader fr = new FileReader("src\\main\\resources\\edu\\vsu\\siuo\\ts.txt")) {
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                if (line.contains("Полный")) {
                    type = "p";
                } else if (line.contains("Уменьшенный")) {
                    type = "u";
                } else if (line.contains("Первый")) {
                    type = "1";
                } else if (line.contains("Второй")) {
                    type = "2";
                } else if (line.contains("Третий")) {
                    type = "3";
                } else if (line.contains("Четвертый")) {
                    type = "4";
                } else if (line.split("\t").length == 4 && !line.contains("Дальн")) {
                    String[] line_ts = line.split("\t");

                    ArrayList<Double> list_ts = new ArrayList<>();
                    list_ts.add(Double.parseDouble(line_ts[0]));
                    list_ts.add(Double.parseDouble(line_ts[1]));
                    list_ts.add(Double.parseDouble(line_ts[2]));
                    list_ts.add(Double.parseDouble(line_ts[3]));

                    switch (type) {
                        case "p":
                            ts.get("Полный").add(list_ts);
                        case "u":
                            ts.get("Уменьшенный").add(list_ts);
                        case "1":
                            ts.get("Первый").add(list_ts);
                        case "2":
                            ts.get("Второй").add(list_ts);
                        case "3":
                            ts.get("Третий").add(list_ts);
                        case "4":
                            ts.get("Четвертый").add(list_ts);
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ts;
    }

    public static ArrayList<Double> ts(String power, double distance) {
        HashMap<String, ArrayList<ArrayList<Double>>> ts = getTS();
        if (distance % 200 > 0) {
            if (distance % 200 > 200 - distance % 200) {
                distance += (200 - distance % 200);
            } else {
                distance -= distance % 200;
            }
        }
        return ts.get(power).get((int) (distance - 600) / 200);
    }
}
