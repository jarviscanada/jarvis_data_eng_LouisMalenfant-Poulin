package ca.jrvs.apps.practice;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {

    @Override
    public Stream<String> createStrStream(String... strings) {
        return Stream.of(strings);
    }

    @Override
    public Stream<String> toUpperCase(String... strings) {
        return createStrStream(strings)
                .map(String::toUpperCase);
    }

    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream
                .filter(str -> !str.contains(pattern));
    }

    @Override
    public IntStream createIntStream(int[] arr) {
        return IntStream.of(arr);
    }

    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.rangeClosed(start, end);
    }

    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream
                .asDoubleStream()
                .map(Math::sqrt);
    }

    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(n -> n % 2 != 0);
    }

    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return message -> System.out.println(prefix + message + suffix);
    }

    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        Stream.of(messages).forEach(printer);
    }

    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        getOdd(intStream)
                .boxed()
                .map(Object::toString)
                .forEach(printer);
    }

    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints
                .flatMap(List::stream)
                .map(n -> n * n);
    }

    public static void main(String[] args) {
        LambdaStreamExc lse = new LambdaStreamExcImp();
        Stream<String> stream = lse.createStrStream("apple", "banana", "cherry");
        List<String> list = lse.toList(stream);
        IntStream intStream = lse.createIntStream(1, 10);
        lse.printOdd(intStream, lse.getLambdaPrinter("Odd: ", "!"));
        }
    }
