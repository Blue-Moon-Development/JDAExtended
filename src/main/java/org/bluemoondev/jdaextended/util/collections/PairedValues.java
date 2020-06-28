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
 * <strong>File:</strong> PairedValues.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class PairedValues<T, E> {

    public T first;
    public E second;

    public PairedValues(T first, E second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof PairedValues)) return false;
        PairedValues<T, E> other = (PairedValues<T, E>) obj;
        return other.first.equals(first) && other.second.equals(second);
    }

}
