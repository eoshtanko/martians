package martians;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Conservator<T> extends Martian<T> {

    final private T genCode;
    final private Martian<T> parent;
    final private List<Martian<T>> children;

    /**
     * Реализует переход из новатора, "который решил, что пришла пора
     * сохранить свою неизменяемую копию для вечности" в консерваторы.
     */
    public Conservator(Innovator<T> novator) {
        genCode = novator.getGenCode();
        parent = null;
        if (novator.getChildren() == null) {
            children = null;
        } else {
            children = novator.getChildren()
                    .stream()
                    .map(n -> new Conservator<T>(n.getGenCode(), this, n.getChildren()))
                    .collect(Collectors.toList());
        }
    }

    public Conservator(T genCode, Martian<T> par, Collection<Martian<T>> ch) {
        this.genCode = genCode;
        parent = par;
        if (ch == null) {
            children = null;
        } else {
            children = ch.stream().map(x -> new Conservator<T>(x.getGenCode(), this, (List<Martian<T>>) x.getChildren()))
                    .collect(Collectors.toList());
        }
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
        boolean res = false;
        for (Martian<T> m : getDescadant()) {
            if (m.getGenCode() == value) {
                res = true;
            }
        }
        return res;
    }
}