package martians;

import java.util.Collection;

public abstract class Martian<T> {

    /**
     * Возвращает генетический код.
     * Каждый имеет генетический код T - тип одинаков для всех членов семьи
     * Значение value кода у каждого марсианина свое (хотя  может и совпадать с кодом какого-нибудь родственника).
     */
    public abstract T getGenCode();

    /**
     * Getter, который возвращает родителя, а если его нет, тогда null.
     */
    public abstract Martian<T> getParent();

    /**
     * Getter, который возвращает Collection всех детей, а если их нет, тогда пустую коллекцию.
     */
    public abstract Collection<Martian<T>> getChildren();

    /**
     * Getter, который возвращает Collection всех потомков, т.е. детей, детей детей и т.д.
     */
    public abstract Collection<Martian<T>> getDescadant();

    /**
     * Метод hasChildWithValue(T value), сообщающий о наличии ребенка с указанным генетическим кодом  (true, если есть, иначе false).
     */
    public abstract boolean hasChildWithValue(T value);

    /**
     * Метод hasDescadantWithValue(T value), сообщающий о наличии потомка с указанным генетическим кодом (true, если есть, иначе false).
     */
    public abstract boolean hasDescadantWithValue(T value);

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getGenCode().getClass().getSimpleName() + ":" + getGenCode() + ")" + "\n";
    }
}
