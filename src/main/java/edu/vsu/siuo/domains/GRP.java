package edu.vsu.siuo.domains;

import lombok.Data;
import lombok.NoArgsConstructor;

import static edu.vsu.siuo.utils.Utils.rand;

@Data
@NoArgsConstructor
public class GRP {
    private int distance_1; //D
    private int difDistance_1; //dD
    private int turn_1; //dd
    private int distance_2;
    private int difDistance_2;
    private int turn_2;
    private int distance_3;
    private int difDistance_3;
    private int turn_3;

    public GRP(int distance_topogr) {
        this.distance_2 = distance_topogr / 1000 + rand(0, 1);
        this.distance_1 = this.distance_2 - 2;
        this.distance_3 = this.distance_2 + 2;

        this.distance_1 = this.distance_1 * 1000;
        this.distance_2 = this.distance_2 * 1000;
        this.distance_3 = this.distance_3 * 1000;

        if (rand(0, 1) == 1) { // поправка на дальность
            this.difDistance_1 = rand(43, 260);
            this.difDistance_2 =  this.difDistance_1 + rand(90, 180);
            this.difDistance_3 =  this.difDistance_2 + rand(90, 180);
        } else {
            this.difDistance_1 = rand(-43, -260);
            this.difDistance_2 =  this.difDistance_1 + rand(-150, -60);
            this.difDistance_3 =  this.difDistance_2 + rand(-150, -60);
        }

        int sign = (rand(0, 1) == 0 ? -1: 1);
        this.turn_1 = rand(sign*3, sign*15);
        this.turn_2 = this.turn_1 + rand(sign*2, sign*8);
        this.turn_3 = this.turn_2 + rand(sign*2, sign*8);
    }
}
