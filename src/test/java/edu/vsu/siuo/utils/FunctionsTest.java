package edu.vsu.siuo.utils;

import edu.vsu.siuo.domains.*;
import edu.vsu.siuo.domains.enums.Powers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionsTest {

    private final double DELTA = 0.01;

    @Test
    public void converseToRad() {
        int angle = 3459;

        double result = Functions.converseToRad(angle);

        assertEquals(3.6222, result, DELTA);
    }

    @Test
    public void converseToDelAngle() {
        double angle = 4.8466924104941;

        int result = Functions.converseToDelAngle(angle);

        assertEquals(4628, result, DELTA);
    }

    @Test
    public void angDash_1() {
        double a = 1.0;

        String result = Functions.angDash(a);

        assertEquals("+0-01", result);
    }

    @Test
    public void angDash_2() {
        double a = -15;

        String result = Functions.angDash(a);

        assertEquals("-0-15", result);
    }

    @Test
    public void angDash_3() {
        double a = 0;

        String result = Functions.angDash(a);

        assertEquals("", result);
    }

    @Test
    public void KmodAngDash_1() {
        double a = -978;

        String result = Functions.modAngDash(a);

        assertEquals("9-78", result);
    }

    @Test
    public void modAngDash_2() {
        double a = 3200;

        String result = Functions.modAngDash(a);

        assertEquals("32-00", result);
    }

    @Test
    public void findDalnUgol_1() {
        double x1 = 65854.021665374;
        double y1 = 85664.027840319;
        double x2 = 64808;
        double y2 = 79738;

        double[] result = Functions.findDalnUgol(x1, y1, x2, y2);

        assertEquals(6017, result[0], DELTA);
        assertEquals(1333, result[1], DELTA);
    }

    @Test
    public void analyzePuo_1() {
        Target target = new Target();
        target.setDistanceFromKNPtoTarget(2819);
        target.setTargetsFrontDu(5526);

        ObjectPosition knp = new ObjectPosition();
        knp.setX(98755);
        knp.setY(40333);

        OP op = new OP();
        op.setMainDirection(5900);
        op.setX(95741);
        op.setY(42768);

        AnalysisResult analysisResult = Functions.analyzePuo(target, knp, null, null, op);

        assertEquals(101, analysisResult.getPs());
        assertEquals(-475, analysisResult.getDovTop());
        assertEquals(6666, analysisResult.getDalTop());
        assertEquals(101233.79267843, analysisResult.getCelX());
        assertEquals(38990.48450386, analysisResult.getCelY());
    }

    @Test
    public void findDalnUgol_2() {
        double x1 = 22001.16889929;
        double y1 = 40472.297986083;
        double x2 = 25253;
        double y2 = 34337;

        double[] result = Functions.findDalnUgol(x1, y1, x2, y2);

        assertEquals(6943, result[0]);
        assertEquals(1965, result[1]);
    }

    @Test
    public void findDalnUgol_3() {
        double x1 = 91683.688147674;
        double y1 = 72580.401827997;
        double x2 = 94975;
        double y2 = 77203;

        double[] result = Functions.findDalnUgol(x1, y1, x2, y2);

        assertEquals(5674, result[0]);
        assertEquals(3909, result[1]);
    }

    @Test
    public void findDalnUgol_4() {
        double x1 = 68367.077235971;
        double y1 = 70661.301797983;
        double x2 = 66640;
        double y2 = 78825;

        double[] result = Functions.findDalnUgol(x1, y1, x2, y2);

        assertEquals(8344, result[0]);
        assertEquals(4699, result[1]);
    }

    @Test
    public void ts_1() {
        Powers power = Powers.Power1;
        double dci = 7029.0;

        ArrayList<Double> result = Functions.ts(power, dci);

        assertEquals(259.61111111111, result.get(1), DELTA);
        assertEquals(18, result.get(2), DELTA);
        assertEquals(17, result.get(3), DELTA);
    }

    @Test
    public void ts_2() {
        Powers gun = Powers.Power2;
        double dci = 5961.0;

        ArrayList<Double> result = Functions.ts(gun, dci);

        assertEquals(263.4, result.get(1), DELTA);
        assertEquals(15, result.get(2), DELTA);
        assertEquals(14, result.get(3), DELTA);
    }

    @Test
    public void v_pricel_1() {
        String result = Functions.vPricel(174);

        assertEquals("+174", result);
    }

    @Test
    public void v_pricel_2() {
        String result = Functions.vPricel(-0.0);

        assertEquals("", result);
    }

    @Test
    public void format_nabl_1() {
        String result = Functions.formatNabl(16, "all_p", 6);

        assertEquals("П16, Все «+», Фр. 0-06", result);
    }

    @Test
    public void format_nabl_2() {
        String result = Functions.formatNabl(88, "one_n", null);

        assertEquals("П88, «-»", result);
    }

    @Test
    public void grpCount_1() {
        int d = 4978;
//        Map<Integer, Map<String, Integer>> grp = new HashMap<>();
//        grp.put(1, Map.of("D", 4247, "dD", -247, "dd", 14));
//        grp.put(0, Map.of("D", 2155, "dD", -155, "dd", 10));
//        grp.put(2, Map.of("D", 6339, "dD", -339, "dd", 20));

        GRP grp = new GRP();
        grp.setDistance_1(4247);
        grp.setDistance_2(2155);
        grp.setDistance_3(6339);

        grp.setDifDistance_1(-247);
        grp.setDifDistance_2(-155);
        grp.setDifDistance_3(-339);

        grp.setTurn_1(14);
        grp.setTurn_2(10);
        grp.setTurn_3(20);

        List<Double> result = Functions.grpCount(grp, d);
        assertEquals(-279, result.get(0));
        assertEquals(-16, result.get(1));
    }

    @Test
    public void grpCount_2() {
        int d = 4612;
//        Map<Integer, Map<String, Integer>> grp = new HashMap<>();
//        grp.put(1, Map.of("D", 5397, "dD", -397, "dd", 17));
//        grp.put(0, Map.of("D", 3255, "dD", -255, "dd", 12));
//        grp.put(2, Map.of("D", 7468, "dD", -468, "dd", 23));

        GRP grp = new GRP();
        grp.setDistance_1(5397);
        grp.setDistance_2(3255);
        grp.setDistance_3(7468);

        grp.setDifDistance_1(-397);
        grp.setDifDistance_1(-255);
        grp.setDifDistance_1(-468);

        grp.setTurn_1(17);
        grp.setTurn_1(12);
        grp.setTurn_1(23);

        List<Double> result = Functions.grpCount(grp, d);
        assertEquals(-345, result.get(0));
        assertEquals(-15, result.get(1));
    }
}