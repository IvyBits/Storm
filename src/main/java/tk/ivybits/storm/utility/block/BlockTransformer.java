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

package tk.ivybits.storm.utility.block;

import org.bukkit.block.Block;

public class BlockTransformer {
    private final IDBlock from;
    private final IDBlock to;

    public BlockTransformer(IDBlock from, IDBlock to) {
        this.from = from;
        this.to = to;
    }

    public boolean transform(Block block) {
        if (block == null)
            return false;
        if (from.isBlock(block)) {
            to.setBlock(block);
            return true;
        }
        return false;
    }

    public static class IDBlock {
        private final int id;
        private int data = -1;

        public IDBlock(int id, int data) {
            this.id = id;
            this.data = data;
        }

        public IDBlock(String data) {
            if (data.contains(":")) {
                String[] split = data.split(":");
                this.id = Integer.parseInt(split[0]);
                this.data = Integer.parseInt(split[1]);
            } else {
                this.id = Integer.parseInt(data);
            }
        }

        public IDBlock(Block b) {
            this.id = b.getTypeId();
            this.data = b.getData();
        }

        public void setBlock(Block b) {
            b.setTypeId(id);
            if (data != -1)
                b.setData((byte) data);
        }

        public boolean isBlock(Block b) {
            return b.getTypeId() == id && data == -1 || b.getData() == data;
        }
    }
}
