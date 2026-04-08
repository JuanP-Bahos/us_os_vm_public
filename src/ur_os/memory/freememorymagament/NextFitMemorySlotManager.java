/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

/**
 * Next Fit Memory Slot Manager.
 *
 * Works like First Fit, but instead of always starting the search from the
 * beginning of the free list, it remembers the index of the last slot that
 * was successfully allocated and starts the next search from there.
 *
 * This distributes allocations more evenly across memory and avoids
 * repeatedly scanning the beginning of the list, which tends to become
 * heavily fragmented over time.
 *
 * 
 */
public class NextFitMemorySlotManager extends FreeMemorySlotManager {

    /**
     * Index of the slot from which the next search will begin.
     * It is updated every time a successful allocation is made.
     */
    private int lastIndex;

    public NextFitMemorySlotManager(int memSize) {
        super(memSize);
        lastIndex = 0;
    }

    @Override
    public MemorySlot getSlot(int size) {
        int n = list.size();

        if (n == 0) {
            System.out.println("Error - No free memory slots available");
            return null;
        }

        // Clamp lastIndex in case slots were removed since the last allocation
        if (lastIndex >= n) {
            lastIndex = 0;
        }

        // Search starting from lastIndex, wrapping around once if needed
        for (int i = 0; i < n; i++) {
            int idx = (lastIndex + i) % n;
            MemorySlot memorySlot = list.get(idx);

            if (memorySlot.canContain(size)) {
                lastIndex = idx; // remember where we stopped

                if (memorySlot.getSize() == size) {
                    // Exact fit: remove the slot entirely and return it
                    list.remove(idx);
                    // If we removed a slot before lastIndex, shift it back
                    if (lastIndex >= list.size() && !list.isEmpty()) {
                        lastIndex = list.size() - 1;
                    }
                    return memorySlot;
                } else {
                    // Partial fit: carve out the requested chunk from the front
                    // assignMemory() shrinks the existing slot in place and
                    // returns a new MemorySlot with the allocated region
                    return memorySlot.assignMemory(size);
                }
            }
        }

        System.out.println("Error - Memory cannot allocate a slot big enough for the requested memory");
        return null;
    }
}
