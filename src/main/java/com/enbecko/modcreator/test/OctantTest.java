package com.enbecko.modcreator.test;

import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 02.03.2017.
 */
public class OctantTest {
    public static void main(String[] args) {
       /** Bone b = new Bone();
        vec3.DoubleVec center = new vec3.DoubleVec(0, -1, -1);
        Octant tmp = new Octant(b, center, 1, 1, 1, Octant.OCTANTS.VIII);
        tmp.addNewChild(new FirstOrderHolder(b, ));
        tmp.setActive(true);
        System.out.println(tmp);*/
       int size = 4;
       vec3.DoubleVec decisiveVec = new vec3.DoubleVec(-1.5, 5, 2);
        vec3 pos = new vec3.DoubleVec(decisiveVec);
        System.out.println(pos);
        pos.divToThis(size);
        vec3 pos1 = (vec3) new vec3.IntVec(pos, true).mulToThis(size);
        System.out.println(pos1);
    }
}
