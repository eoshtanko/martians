package martians;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Я сочла излишним прописывать отдельные методы проверки getParent, getChildren,
// getDescadant, hasChildWithValue, hasDescadantWithValue, changeGenCode.
// Эти методы повторяют логику методов в новаторе и в тестах для новаторов они проверены.
class ConservatorTest {

    @Test
    void fromInovatorToConservTest() {
        System.out.println("Проверка поведения программы при переходе из новаторов в консерваторы.");
        System.out.println("----------------------------------------------------");

        //      Рассматриваем случай дерева:
        //  Innovator(String:i0)
        //      Innovator(String:i1)
        //          Innovator(String:i2)
        //              Innovator(String:i4)
        //          Innovator(String:i3)
        Innovator<String> i0 = new Innovator<>("i0");
        Innovator<String> i1 = new Innovator<>("i1");
        Innovator<String> i2 = new Innovator<>("i2");
        Innovator<String> i3 = new Innovator<>("i3");
        Innovator<String> i4 = new Innovator<>("i4");
        i0.setChild(i1);
        i1.setChild(i2);
        i1.setChild(i3);
        i2.setChild(i4);

        Conservator<String> conservator = new Conservator<>(i1);

        // Проверяем корректно ли скопировались значения
        assertEquals(conservator.getDescadant().size(), 3);
        assertEquals(conservator.getChildren().size(), 2);

        assertTrue(conservator.hasChildWithValue("i2"));
        assertTrue(conservator.hasChildWithValue("i3"));
        assertTrue(conservator.hasDescadantWithValue("i4"));
        // Родитель не перешел в консерваторы
        assertNull(conservator.getParent());
        System.out.println("Все значения копируются корректно. Связи не нарушены.\n" +
                "Значение родителя нового перешедшего консерватора равно null.");
        // Убедимся, иноваторы не повреждены. Их значения просто скопированы.
        assertEquals(i0.getDescadant().size(), 4);
        assertTrue(i1.hasChildWithValue("i2"));
        assertTrue(i0.hasDescadantWithValue("i4"));
        System.out.println("Иноваторы не повреждены. Их значения просто скопированы. ");
        System.out.println("----------------------------------------------------");
        System.out.println("При переходе из новаторов в консерваторы программа работает корректно.");
    }
}