/*
*	jdaextended
*	Copyright (C) 2019 Matt
*
*	This program is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	This program is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*	Contact: matt@bluemoondev.org
 */
package org.bluemoondev.jdaextended.util;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> Util.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Util {

    /**
     * Joins two arrays
     *
     * @param first The array to place at the beginning of the new array
     * @param second The array to place at the end of the new array
     * @return The joined array <code>{ first, second }</code>
     */
    public static String[] joinArray(String[] first, String[] second) {
        String[] result = new String[first.length + second.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = first[i];
        }
        int k = 0;
        for (int i = first.length; i < result.length; i++) {
            result[i] = second[k++];
        }
        return result;
    }

    public static long[] joinArray(long[] first, long[] second) {
        long[] result = new long[first.length + second.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = first[i];
        }
        int k = 0;
        for (int i = first.length; i < result.length; i++) {
            result[i] = second[k++];
        }
        return result;
    }

    public static boolean[] joinArray(boolean[] first, boolean[] second) {
        boolean[] result = new boolean[first.length + second.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = first[i];
        }
        int k = 0;
        for (int i = first.length; i < result.length; i++) {
            result[i] = second[k++];
        }
        return result;
    }

    /**
     * Converts an array to a space separated string
     *
     * @param arr The array to convert
     * @return A string representation of the array
     */
    public static String asString(Object[] arr) {
        if (arr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            sb.append(arr[i]).append(" ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    /**
     * Converts an array to a bracketed, comma separated string
     *
     * @param arr The array to convert
     * @return An array styled string representation of the array
     */
    public static String formatArray(Object[] arr) {
        if (arr.length == 0) {
            return "{ }";
        }
        if (arr.length == 1) {
            return "{ " + arr[0].toString() + " }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{ ").append(arr[0]).append(", ");
        for (int i = 1; i < arr.length - 1; i++) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[arr.length - 1]).append(" }");
        return sb.toString();
    }

    /**
     * Similar to string.split(" ") but keeps quoted elements together.<br>
     * For example
     * <code>splitButKeepQuotes("The red fox \"and the wolf\" ate a \"banana on fire\" after dinner")</code>
     * becomes <strong>{The, red, fox, and the wolf, ate, a, banana on fire,
     * after, dinner}</strong>
     *
     * @param msg The string to split
     * @return msg, split by " " but retaining spaces that are surrounded by
     * quotes
     */
    public static String[] splitButKeepQuotes(String msg) {
        ArrayList<String> resList = new ArrayList<String>();
        String[] first = msg.split(" ");
        int num = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < first.length; i++) {
            if (first[i].contains("\"")) {
                num++;
                sb.append(first[i]);
                if (num < 2) {
                    sb.append(" ");
                }
            } else if (num > 0) {
                sb.append(first[i]).append(" ");
            } else {
                resList.add(first[i]);
            }

            if (num == 2) {
                num = 0;
                resList.add(sb.toString().replaceAll("\"", ""));
                sb = new StringBuilder();
            }
        }

        return resList.stream().toArray(String[]::new);
    }

    /**
     * Check if a certain member has a certain role
     *
     * @param member The member to check if they have a certain role
     * @param id The ID of the role
     * @return True if the member has the role, false otherwise
     */
    public static boolean hasRole(Member member, String id) {
        List<Role> roles = member.getRoles();
        for (Role r : roles) {
            if (r.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the ID from the mention
     *
     * @param mention The mention
     * @return The ID of the user mentioned
     */
    public static String getIdFromMention(String mention) {
        if (mention.contains("!")) {
            return mention.replace("<@!", "").replaceAll(">", "");
        }
        return mention.replace("<@", "").replace(">", "");
    }
    
    public static int getFromHex(String hex) {
    	return Integer.parseInt(hex, 16);
    }

}
