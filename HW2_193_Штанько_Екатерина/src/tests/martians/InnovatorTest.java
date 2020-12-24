package martians;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


class InnovatorTest {

    @Test
    void changeGenCode() {
        System.out.println("Проверка измения значения генетического кода.");
        System.out.println("----------------------------------------------------");

        Innovator<String> innovator = new Innovator<>("Mike");
        innovator.setGenCode("Sofy");
        assertEquals("Sofy", innovator.getGenCode());

        innovator.setGenCode(null);
        assertNull(innovator.getGenCode());

        System.out.println("Значение генетического ключа меняется корректно.\n");
    }

    @Test
    void setAndGetParentTest() {
        System.out.println("Проверка присваивания родителя.");
        System.out.println("----------------------------------------------------");

        Innovator<String> innovatorPar = new Innovator<>("Mike");
        Innovator<String> innovatorChild = new Innovator<>("Jack");
        Innovator<String> innovatorChildOfChild = new Innovator<>("Ы");

        assertNull(innovatorChild.getParent());
        System.out.println("Если родитель у иноватора не установлен - возврат null.");

        assertFalse(innovatorPar.setParent(null));
        System.out.println("При попытке установить родителем значение null - false.");

        assertFalse(innovatorPar.setParent(innovatorPar));
        System.out.println("Программа не позволяет установить самого себя своим ребенком, что предотвращает петли.");

        // корректная установка
        assertTrue(innovatorChild.setParent(innovatorPar));
        assertTrue(innovatorChildOfChild.setParent(innovatorChild));

        // проверка, корректную информацию имеет ребенок о новорм родителе
        assertEquals(innovatorPar, innovatorChild.getParent());
        // проверка информации, которую родитель имеет о ребенке
        assertTrue(innovatorPar.getChildren().contains(innovatorChild));
        assertTrue(innovatorPar.getDescadant().contains(innovatorChild));
        System.out.println("Установка родителя(setParent) проходит успешно.\nРебенок имеет корректную информацию о родителе." +
                " \nРебенок появляется в списке детей родителя.\nУстановленного ребенка можно также увидеть, " +
                "запросив список всех предков родителя.");

        assertFalse(innovatorChild.setParent(innovatorPar));
        System.out.println("Программа не позволяет присвоить ребенку того родителя, который уже числится у него.");

        assertFalse(innovatorPar.setParent(innovatorChild));
        System.out.println("Программа не позволяет сделать родителем марсианина, который числится в детях(устойчивость к циклам).");

        assertFalse(innovatorPar.setParent(innovatorChildOfChild));
        System.out.println("Программа не позволяет сделать родителем марсианина, который числится в потомках(устойчивость к циклам).");

        System.out.println("----------------------------------------------------");
        System.out.println("Проверка присваивания родителя завершилась успешно.\n");
    }

    @Test
    void setAndGetChildTest() {
        System.out.println("Проверка присваивания ребенка.");
        System.out.println("----------------------------------------------------");

        Innovator<String> innovatorPar = new Innovator<>("Mike");
        Innovator<String> innovatorChild = new Innovator<>("Jack");
        Innovator<String> innovatorChildOfChild = new Innovator<>("Ы");

        assertNull(innovatorPar.getChildren());
        assertNull(innovatorPar.getDescadant());
        System.out.println("Если у иноватора еще нет детей(и потомков), при попытке их получить - возврат null.");

        assertFalse(innovatorPar.hasChildWithValue("Jack"));
        assertFalse(innovatorPar.hasDescadantWithValue("Jack"));
        System.out.println("При поиске отсутствующего ребенка/потомка по ключу - возарат false.");

        assertFalse(innovatorPar.setChild(innovatorPar));
        System.out.println("Программа не позволяет установить самого себя своим ребенком(устойчивость к петлям).");

        assertFalse(innovatorPar.setChild(null));
        System.out.println("При попытке установить ребенком значение null - false.");

        // корректная установка детей
        assertTrue(innovatorPar.setChild(innovatorChild));
        assertTrue(innovatorChild.setChild(innovatorChildOfChild));

        // проверка корректности информации, которую ребенок имеет о родителе
        assertEquals(innovatorPar, innovatorChild.getParent());
        // проверка корректности информации, которую родитель имеет о ребенке
        assertTrue(innovatorPar.getChildren().contains(innovatorChild));
        assertTrue(innovatorPar.getDescadant().contains(innovatorChild));
        System.out.println("Установка родителя(setParent) проходит успешно.\nРебенок имеет корректную информацию о родителе." +
                " \nРебенок появляется в списке детей родителя.\nУстановленного ребенка можно также увидеть, " +
                "запросив список всех предков родителя.");

        assertTrue(innovatorPar.hasChildWithValue("Jack"));
        assertTrue(innovatorPar.hasDescadantWithValue("Jack"));
        System.out.println("При поиске присутствующего ребенка/потомка по ключу - возарат true.");

        assertFalse(innovatorPar.setChild(innovatorChild));
        System.out.println("Программа не позволяет присвоить родителю того ребенка, который уже числится у него.");

        assertFalse(innovatorChild.setChild(innovatorPar));
        System.out.println("Программа не позволяет сделать ребенком марсианина, который числится в родителях(устойчивость к циклам).");

        assertFalse(innovatorChildOfChild.setChild(innovatorPar));
        System.out.println("Программа не позволяет сделать ребенком марсианина, который числится в предках," +
                " например, родителя родителя(устойчивость к циклам).");

        System.out.println("----------------------------------------------------");
        System.out.println("Проверка присваивания ребенка завершилась успешно.\n");
    }

    @Test
    void delChildTest() {
        System.out.println("Проверка удаления ребенка.");
        System.out.println("----------------------------------------------------");

        Innovator<String> innovatorPar = new Innovator<>("Mike");
        Innovator<String> innovatorChild = new Innovator<>("Jack");
        Innovator<String> innovatorChildOfChild = new Innovator<>("Lily");

        assertFalse(innovatorPar.delChild(innovatorChild));
        System.out.println("Программа возвращает false при попытке удаления отсутствующего ребенка.");

        // устанавливаем связи
        innovatorChild.setParent(innovatorPar);
        innovatorChildOfChild.setParent(innovatorChild);

        assertTrue(innovatorPar.delChild(innovatorChild));
        System.out.println("Программа возвращает true при удалении ребенка.");

        assertFalse(innovatorPar.hasChildWithValue("Jack"));
        assertFalse(innovatorPar.hasDescadantWithValue("Jack"));
        System.out.println("Ребенок корректно удаляется из списка детей и потомков.");

        assertFalse(innovatorPar.hasDescadantWithValue("Lily"));
        System.out.println("Дети удаленного ребенка корректно удаляются из списка потомков.");

        assertNull(innovatorChild.getParent());
        System.out.println("При попытке получить родителя у удаленного ребенка - возврат null.");

        System.out.println("----------------------------------------------------");
        System.out.println("Проверка удаления ребенка завершилась успешно.\n");
    }

    @Test
    void setAndGetDescadantTest() {
        System.out.println("Проверка работы с потомками.");
        System.out.println("----------------------------------------------------");

        Innovator<String> i = new Innovator<>("Mike");

        assertFalse(i.setDescadant(null));
        System.out.println("При попытке установить потомками null - false.");

        assertFalse(i.setDescadant(new ArrayList<Innovator<String>>()));
        System.out.println("При попытке установить потомками пустой список - false.");
        // Рассмотри случай простого дерева: 5 элементов последовательно соеденены
        DescadantTestTree1();
        DescadantTestTree2();
        System.out.println("----------------------------------------------------");
        System.out.println("Программа корректно работает с потомками.\n");
    }

    /**
     * Вспомогательный метод для тестирования работы с потомками.
     * Рассматривает случай простого дерева: 5 элементов последовательно соеденены
     */
    void DescadantTestTree1() {
        Innovator<String> i1 = new Innovator<>("Mike");
        Innovator<String> i2 = new Innovator<>("Jack");
        Innovator<String> i3 = new Innovator<>("Lily");
        Innovator<String> i4 = new Innovator<>("Ы");
        Innovator<String> i5 = new Innovator<>("Kate");
        i5.setParent(i4);
        i4.setParent(i3);
        i3.setParent(i2);
        i2.setParent(i1);
        // для сравнения в будующем
        Collection<Martian<String>> temdec = i1.getDescadant();
        Collection<Martian<String>> temch = i1.getChildren();

        ArrayList<Innovator<String>> collOfDescadant = new ArrayList<>();
        collOfDescadant.add(i5);
        collOfDescadant.add(i4);
        assertFalse(i1.setDescadant(collOfDescadant));
        System.out.println("При попытке присвоить потомков, которые уже имеются - false.");

        collOfDescadant = new ArrayList<>();
        collOfDescadant.add(i1);
        collOfDescadant.add(i2);
        assertFalse(i5.setDescadant(collOfDescadant));
        System.out.println("При попытке присвоить потомками предков - false (устойчивость к циклам).");

        // "разъединяем" дерево
        i1.delChild(i2);

        collOfDescadant = new ArrayList<>();
        collOfDescadant.add(i5);
        collOfDescadant.add(i3);
        assertFalse(i1.setDescadant(collOfDescadant));
        System.out.println("При попытке присвоить потомками неполную родственную коллекцию  - false.\n" +
                "    Пример \"неполной родственной коллекции\":\n" +
                "    В коллекции присутствуют \"ребенок\" и \"дедушка\", а \"родитель\" пропущен.");

        // "Восстановим" дерево и проведем сравнение
        collOfDescadant = new ArrayList<>();
        collOfDescadant.add(i5);
        collOfDescadant.add(i4);
        collOfDescadant.add(i3);
        collOfDescadant.add(i2);
        assertTrue(i1.setDescadant(collOfDescadant));
        // сравниваем с изначальным цельным деревом
        // temdec - потомки i1 изначально
        // temch - дети i1 изначально
        assertEquals(i1.getDescadant().size(), temdec.size());
        assertEquals(i1.getChildren().size(), temch.size());
        System.out.println("Потомки добавляются корректно.\nОни появляются в списке детей/потомков.");

        // проверка некоторых моментов, которые позволят нам убедится, что связи не нарушены
        assertTrue(i2.getChildren().contains(i3));
        assertEquals(i2.getChildren().size(), 1);
        assertEquals(i2.getDescadant().size(), 3);
        assertEquals(i5.getParent(), i4);
        System.out.println("Cвязь между ними не нарушена.");

        assertEquals(i2.getParent(), i1);
        System.out.println("\"Прикрепляемый\" эдемент считает того, к которому он был прикреплен, родителем.");
    }

    /**
     * Вспомогательный метод для тестирования работы с потомками.
     * Рассматривает случай дерева:
     * Innovator(String:i1)
     * Innovator(String:i2)
     * Innovator(String:i4)
     * Innovator(String:i3)
     */
    void DescadantTestTree2() {
        Innovator<String> i1 = new Innovator<>("i1");
        Innovator<String> i2 = new Innovator<>("i2");
        Innovator<String> i3 = new Innovator<>("i3");
        Innovator<String> i4 = new Innovator<>("i4");
        i1.setChild(i2);
        i1.setChild(i3);
        i2.setChild(i4);

        ArrayList<Innovator<String>> collOfDescadant = new ArrayList<>();
        collOfDescadant.add(i2);
        assertFalse(i3.setDescadant(collOfDescadant));
        System.out.println("При попытке присвоить потомками неполную родственную коллекцию  - false.\n" +
                "    Другой пример \"неполной родственной коллекции\":\n" +
                "    В коллекции присутствуют \"родитель\" и \"дедушка\", а \"ребенок\" пропущен.");
        collOfDescadant.add(i4);
        // Устанавливаем корректную коллекцию потомков
        assertTrue(i3.setDescadant(collOfDescadant));
        // Проверяем, что все установилось корректно
        assertEquals(i1.getChildren().size(), 1);
        assertTrue(i3.getChildren().contains(i2));
    }
}