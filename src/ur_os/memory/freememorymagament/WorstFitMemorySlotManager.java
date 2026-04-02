/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

/**
 * @author super
 */
public class WorstFitMemorySlotManager extends FreeMemorySlotManager {

  public WorstFitMemorySlotManager(int memSize) {
    super(memSize);
  }

  @Override
  public MemorySlot getSlot(int size) {
    MemorySlot m = null;

    MemorySlot worstSlot = null;
    int worstAmount = Integer.MIN_VALUE;

    for (MemorySlot memorySlot : list) {
      if (memorySlot.getSize() >= worstAmount && memorySlot.canContain(size)) {
        worstSlot = memorySlot;
        worstAmount = memorySlot.getSize();
      }
    }

    if (worstSlot == null) {
      return null;
    }

    if (worstSlot.getSize() == size) {
      m = worstSlot;
      list.remove(m);
      return m;
    } else {
      m = worstSlot.assignMemory(size);
      return m;
    }
  }
}
