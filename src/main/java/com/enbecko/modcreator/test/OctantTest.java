package com.enbecko.modcreator.test;

import com.enbecko.modcreator.geometry.Bone;
import com.enbecko.modcreator.geometry.FirstOrderHolder;
import com.enbecko.modcreator.geometry.Octant;
import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 02.03.2017.
 */
public class OctantTest {
    public static void main(String[] args) {
        Bone b = new Bone();
        vec3.DoubleVec center = new vec3.DoubleVec(0, -1, -1);
        Octant tmp = new Octant(b, center, 1, 1, 1, Octant.OCTANTS.VIII);
        tmp.addContent(new FirstOrderHolder(b));
        tmp.setActive(true);
        System.out.println(tmp);
    }
}
