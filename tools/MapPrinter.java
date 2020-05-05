/*
 * Copyright (C) 2015 iGate
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package ceeport.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sean
 * http://stackoverflow.com/questions/3605237/how-print-out-the-contents-of-a-hashmapstring-string-in-ascending-order-based
 */
public class MapPrinter {

    public static <K, V extends Comparable<? super V>>
            Comparator<K> mapValueComparator(final Map<K, V> map) {
        return new Comparator<K>() {
            public int compare(K key1, K key2) {
                return map.get(key1).compareTo(map.get(key2));
            }
        };
    }

    public static <K, V>
            Comparator<K> mapValueComparator(final Map<K, V> map,
                    final Comparator<V> comparator) {
        return new Comparator<K>() {
            public int compare(K key1, K key2) {
                return comparator.compare(map.get(key1), map.get(key2));
            }
        };
    }

    public static void main(String[] args) {
        Map<String, String> map = getMyMap();
        List<String> keys = new ArrayList<String>(map.keySet());
        //Collections.sort(keys, someComparator);
        for (String key : keys) {
            System.out.println(key + ": " + map.get(key));
        }
    }

    private static Map<String, String> getMyMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
