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
package org.bluemoondev.jdaextended.util.collections;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> PairedValue.java
 *
 * <p>
 * An object which stores two values of the same type
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class PairedValue<T> {

    public T first;
    public T second;

    public PairedValue(T first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof PairedValue)) return false;
        PairedValue<T> other = (PairedValue<T>) obj;
        return other.first.equals(first) && other.second.equals(second);
    }

}
