/*
 * This file is part of Storm.
 *
 * Storm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Storm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Storm.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package tk.ivybits.storm.utility;

import tk.ivybits.storm.Storm;

import java.util.logging.Level;

public class Verbose {

    public static void log(Level lev, Object mes) {
        if (Storm.verbose)
            StormUtil.log(lev, mes + "");
    }

    public static void log(Object mes) {
        if (Storm.verbose)
            StormUtil.log(mes + "");
    }
}
