package tree;

import martians.Conservator;
import martians.Innovator;
import martians.Martian;

import java.util.ArrayList;

public class FamilyTree {
    private static Martian root;
    private static final String WHITESPACES = "    ";
    final static private Integer LENGTH = 256;

    public static void main(String[] args) {
        String s = "Innovator(String:root)\n" +
                "    Innovator(String:a)\n" +
                "        Innovator(String:c)\n" +
                "            Innovator(String:e)\n" +
                "        Innovator(String:d)\n" +
                "    Innovator(String:b)";
        textToTree(s);
        Innovator<String> i0 = new Innovator<>("i0");
        Innovator<String> i1 = new Innovator<>("i1");
        Innovator<String> i2 = new Innovator<>("i2");
        Innovator<String> i3 = new Innovator<>("i3");
        Innovator<String> i4 = new Innovator<>("i4");
        i0.setChild(i1);
        i1.setChild(i2);
        i1.setChild(i3);
        i2.setChild(i4);
        textReport(i0);
    }

    /**
     * Предоставляет это дерево в виде текстового отчета и выводит результат в консоль.
     */
    public static String textReport(Martian root) {
        StringBuilder res = new StringBuilder();
        if (root != null) {
            res.append(root);
            if (root.getChildren() != null) {
                for (var ch : root.getChildren()) {
                    amountOfWS = 1;
                    res.append(WHITESPACES).append(printChildren((Martian) ch));
                }
            }
        }
        System.out.println(res);
        return res.toString();
    }

    /**
     * Вспомогательный метод для предоставления дерева в виде текстового отчета.
     * Рекурсивно "записывает" всех детей.
     */
    static String printChildren(Martian par) {
        amountOfWS += 1;
        StringBuilder res = new StringBuilder(par.toString());
        if (par != null && par.getChildren() != null) {
            int curAmount = amountOfWS;
            for (var ch : par.getChildren()) {
                res.append(WHITESPACES.repeat(curAmount)).append(printChildren((Martian) ch));
            }
        }
        return res.toString();
    }

    /**
     * Вспомогательное поле для вывода отчета, содержащая кол-во пробелов
     */
    static int amountOfWS;

    /**
     * Вспомогательный метод перевода отчета в марсиан. Рекурсивно парсит детей.
     */
    static <typeOfGen> int parceChildren(int numberInInput, Innovator<typeOfGen> parent, int amountOfWS) {
        while (numberInInput < report.size() && report.get(numberInInput).startsWith(" ".repeat(amountOfWS))) {
            String toParce = report.get(numberInInput).substring(amountOfWS);
            ArrayList<String> afterParse = parceString(toParce);
            Innovator<typeOfGen> newInovator = new Innovator<>((typeOfGen) afterParse.get(2));
            parent.setChild(newInovator);
            numberInInput = parceChildren(++numberInInput, newInovator, amountOfWS + 4);
            numberInInput++;
        }
        return numberInInput - 1;
    }

    /**
     * отчет
     */
    static ArrayList<String> report = new ArrayList<>();

    /**
     * создает новое симейство иновтаоров
     */
    static <typeOfGen> void creatsNewInovatorFamily() {
        ArrayList<String> firstInovator = parceString(report.get(0));
        Innovator<typeOfGen> rootInovator = new Innovator<>((typeOfGen) firstInovator.get(2));
        root = rootInovator;
        parceChildren(1, rootInovator, 4);
    }

    /**
     * парсит строку типа InnovatorMartian (String:John)
     */
    static ArrayList<String> parceString(String str) {
        StringBuilder res = new StringBuilder();
        ArrayList<String> output = new ArrayList<>();
        char[] arr = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (arr[i] != ')' && arr[i] != '(' && arr[i] != ':') {
                res.append(arr[i]);
            } else {
                output.add(res.toString());
                res = new StringBuilder();
            }
        }
        if (output.size() != 3)
            throw new IllegalArgumentException("Ошибка! Верный формат: <SimpleRootClassName>(<SimpleObjectClassName>:<ObjectToString>)");
        return output;
    }

    /**
     * Парсит строку в лист строк
     */
    static ArrayList<String> fromStringToList(String str) {
        StringBuilder res = new StringBuilder();
        ArrayList<String> output = new ArrayList<>();
        char[] arr = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (arr[i] != '\n') {
                res.append(arr[i]);
            } else {
                output.add(res.toString());
                res = new StringBuilder();
            }
        }
        if (res.length() > 1)
            output.add(res.toString());
        return output;
    }

    /**
     * Определяет тип и запускает парсинг
     */
    static void findOutTypeAndStart(ArrayList<String> firstInovator) {
        switch (firstInovator.get(1)) {
            case "String":
                checkString();
                FamilyTree.<String>creatsNewInovatorFamily();
                break;
            case "Double":
                checkDouble();
                FamilyTree.<Double>creatsNewInovatorFamily();
                break;
            case "Integer":
                checkInt();
                FamilyTree.<Integer>creatsNewInovatorFamily();
                break;
            default:
                throw new IllegalArgumentException("Ошибка! SimpleObjectClassName: String, Double и Integer.");
        }
    }

    /**
     * Переводит отчет в объект класса генеалогического дерева.
     */
    public static void textToTree(String stringReport) {
        try {
            if (stringReport == null || stringReport.equals("")) {
                throw new IllegalArgumentException("Ошибка! Отчет пуст.");
            }
            report = fromStringToList(stringReport);
            checkIfSame();
            ArrayList<String> firstInovator = parceString(report.get(0));
            if (firstInovator.get(0).equals("Innovator")) {
                findOutTypeAndStart(firstInovator);
            } else if (firstInovator.get(0).equals("Conservator")) {
                findOutTypeAndStart(firstInovator);
                root = new Conservator((Innovator) root);
            } else {
                throw new IllegalArgumentException("Ошибка! У нас могут быть только иноваторы и консерваторы.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Проверяет все ли марсиани одного типа и имеют один тип ген кода
     */
    static void checkIfSame() {
        ArrayList<String> mart1 = parceString(report.get(0));
        for (var s : report) {
            ArrayList<String> mart = parceString(s);
            String type1 = mart.get(0);
            if (!type1.replaceAll(" ", "").equals(mart1.get(0)) && !mart.get(1).equals(mart1.get(1))) {
                throw new IllegalArgumentException("Ошибка! В семье могут быть только марсиани одного типа.");
            }
        }
    }

    /**
     * Проверяет, действительно ли все ген. коды - double
     */
    static void checkDouble() {
        for (var s : report) {
            ArrayList<String> mart = parceString(s);
            try {
                String ch = mart.get(2);
                if (!mart.get(2).contains(".")) {
                    ch = mart.get(2) + ".0";
                }
                Double.parseDouble(ch);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Обещали Double, а дали не Double.");
            }
        }
    }

    /**
     * Проверяет, действительно ли все ген. коды - int
     */
    static void checkInt() {
        for (var s : report) {
            ArrayList<String> mart = parceString(s);
            try {
                Integer.parseInt(mart.get(2));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Обещали Int, а дали не Int.");
            }
        }
    }

    /**
     * Проверяет длину строки
     */
    static void checkString() {
        for (var s : report) {
            ArrayList<String> mart = parceString(s);
            if (mart.get(2).length() > LENGTH) {
                throw new IllegalArgumentException("В генетическом коде из отчета 256 знаков максимум");
            }
        }
    }
}
