package martians;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Innovator<T> extends Martian<T> {

    private T genCode;
    private Innovator<T> parent;
    private List<Martian<T>> children;

    public Innovator(T gen) {
        genCode = gen;
    }

    /**
     * Возвращает генетический код.
     * Каждый имеет генетический код T - тип одинаков для всех членов семьи
     * Значение value кода у каждого марсианина свое (хотя  может и совпадать с кодом какого-нибудь родственника).
     */
    @Override
    public T getGenCode() {
        return genCode;
    }

    /**
     * Getter, который возвращает родителя, а если его нет, тогда null.
     */
    @Override
    public Martian<T> getParent() {
        return parent;
    }

    /**
     * Getter, который возвращает Collection всех детей, а если их нет, тогда пустую коллекцию.
     */
    @Override
    public Collection<Martian<T>> getChildren() {
        if (children == null) {
            return null;
        }
        return Collections.unmodifiableCollection(children);
    }

    /**
     * Getter, который возвращает Collection всех потомков, т.е. детей, детей детей и т.д.
     */
    @Override
    public Collection<Martian<T>> getDescadant() {
        if (children == null) {
            return null;
        }
        Collection<Martian<T>> res = new ArrayList<>();
        for (Martian<T> m : children) {
            if (m.getDescadant() != null)
                res.addAll(m.getDescadant());
        }
        res.addAll(children);
        return res;
    }

    /**
     * Метод hasChildWithValue(T value), сообщающий о наличии ребенка с указанным генетическим кодом  (true, если есть, иначе false).
     */
    @Override
    public boolean hasChildWithValue(T value) {
        if (children == null)
            return false;
        for (Martian<T> m : children) {
            if (m.getGenCode() == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод hasDescadantWithValue(T value), сообщающий о наличии потомка с указанным генетическим кодом (true, если есть, иначе false).
     */
    @Override
    public boolean hasDescadantWithValue(T value) {
        if (getDescadant() == null)
            return false;
        for (Martian<T> m : getDescadant()) {
            if (m.getGenCode() == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * setter, который устанавливает новый генетический код.
     *
     * @param code новый генетический код
     */
    public void setGenCode(T code) {
        genCode = code;
    }

    /**
     * setter, который позволяет изменить родителя данного новатора.
     *
     * @param parent новый родитель
     * @return true, если установка прошла успешно, иначе - false
     */
    public boolean setParent(Innovator<T> parent) {
        if (parent == null) {
            return false;
        }
        // Проверка на петлю
        if (parent.equals(this)) {
            return false;
        }
        // Если этот родитель уже установлен - возвращаем false.
        if (this.parent != null && this.parent.equals(parent)) {
            return false;
        }
        // Еслм у новатора потенциальный родитель числится в отпрысках - низя
        if (getDescadant() != null && getDescadant().contains(parent)) {
            return false;
        }
        if (this.parent != null) {
            // Удаляем текцщенго новатора у его родителя
            this.parent.delChild(this);
        }
        // Устанавливаем нового родителя
        this.parent = parent;
        if (this.parent.getChildren() == null) {
            this.parent.children = new ArrayList<>();
        }
        // Проверка для избежания рекурсии
        if (!this.parent.getChildren().contains(this)) {
            // Оповещаем нового родителя о новом ребенке
            this.parent.setChild(this);
        }
        return true;
    }

    /**
     * setter, устанавливающий Collection новых потомков новатору
     *
     * @param descadant коллекция новых потомков
     * @return true, если установка прошла успешно, иначе - false
     */
    public boolean setDescadant(Collection<Innovator<T>> descadant) {
        if (descadant == null || descadant.size() == 0) {
            return false;
        }

        int size = descadant.size();
        // Осуществляем проверку на повторы и пустые элементы
        descadant = descadant.stream().distinct().collect(Collectors.toList());
        descadant.remove(null);
        if (descadant.size() != size) {
            return false;
        }

        // Проверяем, цельна ли переданная коллекция родственников
        for (var m : descadant) {
            if (m.getDescadant() != null && !descadant.containsAll(m.getDescadant())) {
                return false;
            }
        }

        // Проверяем не содержатся ли среди переданных потомков те, что уже присутствуют
        if (this.getDescadant() != null) {
            for (var m : descadant) {
                if (this.getDescadant().contains(m)) {
                    return false;
                }
            }
        }
        // Добавляем потомков
        Collection<Innovator<T>> descadants = new ArrayList<>();
        for (var d : descadant) {
            if (!descadant.contains(d.parent)) {
                descadants.add(d);
            }
        }
        // Проверка на цикл
        Martian<T> thisParent = this.parent;
        while (thisParent != null) {
            if (descadants.contains(thisParent)) {
                return false;
            }
            thisParent = thisParent.getParent();
        }
        if (children != null) {
            int length = children.size();
            // удоляем детей
            for (int i = 0; i < length; i++) {
                delChild((Innovator<T>) getChildren().toArray()[0]);
            }
        }
        for (var d : descadants) {
            setChild(d);
        }
        return true;
    }

    /**
     * Добавляет указанного ребенка к данному новатору.
     *
     * @param child новый ребенок
     * @return true - если добавление указанного ребенка к данному новатору прошло успешно,
     * false - в обратном случае
     */
    public boolean setChild(Innovator<T> child) {
        if (child == null)
            return false;
        // Если ребенок уже установлен
        if (children != null && getChildren().contains(child)) {
            return false;
        }
        // Проверка на петлю
        if (child.equals(this)) {
            return false;
        }
        // Проверка, не содержится ли в предках данного новатора указанный ребенок
        Martian<T> par = this.parent;
        while (par != null) {
            if (par == child) {
                return false;
            }
            par = par.getParent();
        }
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        // Для избежания рекурсии, проверяем не установили ли мы уже данноую связь
        if (child.getParent() != this) {
            if (child.getParent() != null) {
                ((Innovator<T>) child.getParent()).delChild(child);
            }
            child.setParent(this);
        }
        return true;
    }

    /**
     * Удаляяет указанного ребенка у данного новатора.
     *
     * @param child ребенок для удаления
     * @return true - если удаление указанного ребенка у данного новатора прошло успешно,
     * false - в обратном случае
     */
    public boolean delChild(Innovator<T> child) {
        if (child == null)
            return false;
        if (children == null)
            return false;
        if (children.contains(child)) {
            child.parent = null;
            children.remove(child);
            return true;
        }
        return false;
    }
}
