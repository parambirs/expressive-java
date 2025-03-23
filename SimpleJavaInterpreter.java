import java.util.*;
import java.util.function.Function;

public interface SimpleJavaInterpreter {

    record M<A>(A value) {
        <B> M<B> bind(Function<A, M<B>> k) {
            return k.apply(value);
        }

        <B> M<B> map(Function<A, B> f) {
            return bind(x -> unitM(f.apply(x)));
        }

        <B> M<B> flatMap(Function<A, M<B>> f) {
            return bind(f);
        }
    }

    static <A> M<A> unitM(A a) {
        return new M<>(a);
    }

    static String showM(M<Value> m) {
        return m.value.toString();
    }

    sealed interface Term permits Var, Con, Add, Lam, App { }
    record Var(String x) implements Term { }
    record Con(int n) implements Term { }
    record Add(Term l, Term r) implements Term { }
    record Lam(String x, Term body) implements Term { }
    record App(Term fun, Term arg) implements Term { }

    sealed interface Value permits Num, Fun, Wrong { }

    record Wrong() implements Value {
        public static final Wrong INSTANCE = new Wrong();

        @Override
        public String toString() {
            return "wrong";
        }
    }

    record Num(int n) implements Value {
        @Override
        public String toString() {
            return Integer.toString(n);
        }
    }

    record Fun(Function<Value, M<Value>> f) implements Value {
        @Override
        public String toString() {
            return "<function>";
        }
    }
    
    static M<Value> lookup(String x, Map<String, Value> e) {
        return switch (e) {
            case Map __ when !e.containsKey(x) -> unitM(Wrong.INSTANCE);
            default -> unitM(e.get(x));
        };
    }

    static M<Value> add(Value a, Value b) {
        return switch (new Pair<>(a, b)) {
            case Pair<Value, Value>(Num(var m), Num(var n)) -> unitM(new Num(m + n));
            default -> unitM(Wrong.INSTANCE);
        };
    }

    static M<Value> apply(Value a, Value b) {
        return switch (a) {
            case Fun(var k) -> k.apply(b);
            default -> unitM(Wrong.INSTANCE);
        };
    }

    static M<Value> interp(Term t, Map<String, Value> e) {
        return switch (t) {
            case Var(var x) -> lookup(x, e);
            case Con(var n) -> unitM(new Num(n));
            case Add(var l, var r) -> {
                var a = interp(l, e);
                var b = interp(r, e);
                yield add(a.value, b.value);
            }
            case Lam(var x, var term) -> unitM(new Fun(a -> interp(term, updatedMapWith(e, x, a))));
            case App(var f, var arg) -> {
                var a = interp(f, e);
                var b = interp(arg, e);
                yield apply(a.value, b.value);
            }
        };
    }

    static String test(Term t) {
        return showM(interp(t, Map.of()));
    }

    static void main(String[] args) {
        var term0 = App(Lam("x", Add(Var("x"), Var("x"))), Add(Con(10), Con(11)));
        var term1 = App(Con(1), Con(2));

        System.out.println(test(term0));
        System.out.println(test(term1));
    }
    
    // Utility records, methods
    record Pair<A, B>(A a, B b) {}

    static Map<String, Value> updatedMapWith(Map<String, Value> e, String k, Value v) {
        Map<String, Value> result = new HashMap<>(e);
        result.put(k, v);
        return result;
    }
    
    static App App(Term fun, Term arg) {
        return new App(fun, arg);
    }
    
    static Lam Lam(String x, Term body) {
        return new Lam(x, body);
    }
    
    static Add Add(Term l, Term r) {
        return new Add(l, r);
    }
    
    static Con Con(int n) {
        return new Con(n);
    }
    
    static Var Var(String x) {
        return new Var(x);
    }
    
    static <T> List<T> cons(T value, List<T> list) {
        var worker = new ArrayList<T>();
        worker.add(value);
        worker.addAll(list);
        return Collections.unmodifiableList(worker);
    }

}
