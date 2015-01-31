/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MatchTest {

	// -- null handling

	@Test
	public void shouldMatchNullAsPrototype() {
		final int actual = Match.caze((String s) -> s.length()).caze(null, o -> 1).apply(null);
		assertThat(actual).isEqualTo(1);
	}

	@Test(expected = MatchError.class)
	public void shouldNotMatchNullAsType() {
		Match.caze((int i) -> false).caze((Integer i) -> true).apply(null);
	}

	// -- no match

	@Test(expected = MatchError.class)
	public void shouldThrowOnNoMatchByValue() {
		Match.caze("1", o -> 1).apply("2");
	}

	@Test
	public void shouldGetObjectWhenMatchErrorOccurs() {
		try {
			Match.caze("1", o -> 1).apply("2");
			fail("No MatchError thrown");
		} catch (MatchError x) {
			assertThat(x.getObject()).isEqualTo("2");
		}
	}

	// -- match by type of function

	@Test
	public void shouldMatchByDoubleOnMultipleCasesUsingTypedParameter() {
		final int actual = Match.caze((Byte b) -> 1).caze((Double d) -> 2).caze((Integer i) -> 3).apply(1.0d);
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldMatchByIntOnMultipleCasesUsingTypedParameter() {
		final int actual = Match
				.caze((Byte b) -> (int) b)
				.caze((Double d) -> d.intValue())
				.caze((Integer i) -> i)
				.apply(Integer.MAX_VALUE);
		assertThat(actual).isEqualTo(Integer.MAX_VALUE);
	}

	@Test
	public void shouldMatchByAssignableTypeOnMultipleCases() {
		final int actual = Match.caze(1, o -> 'a').caze((Number n) -> 'b').caze((Object o) -> 'c').apply(2.0d);
		assertThat(actual).isEqualTo('b');
	}

	// -- default case

	@Test
	public void shouldMatchDefaultCase() {
		final int actual = Match.caze(null, o -> 1).orElse(() -> 2).apply("default");
		assertThat(actual).isEqualTo(2);
	}

	// -- generics vs type erasure

	@Test
	public void shouldClarifyHereThatTypeErasureIsPresent() {
		final int actual = Match
				.caze((Some<Integer> some) -> 1)
				.caze((Some<String> some) -> Integer.parseInt(some.get()))
				.apply(new Some<>("123"));
		assertThat(actual).isEqualTo(1);
	}

	// -- primitive types vs objects

	// boolean / Boolean

	@Test
	public void shouldMatchPrimitiveBoolean() {
		final boolean actual = Match.caze((boolean b) -> true).caze((Boolean b) -> false).apply(true);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveBooleanAsBoolean() {
		final boolean actual = Match.caze((Boolean b) -> true).caze((boolean b) -> false).apply(true);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBooleanAsPrimitiveBoolean() {
		final boolean actual = Match.caze((boolean b) -> true).caze((Boolean b) -> false).apply(Boolean.TRUE);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoolean() {
		final boolean actual = Match.caze((Boolean b) -> true).caze((boolean b) -> false).apply(Boolean.TRUE);
		assertThat(actual).isTrue();
	}

	// byte / Byte

	@Test
	public void shouldMatchPrimitiveByte() {
		final boolean actual = Match.caze((byte b) -> true).caze((Byte b) -> false).apply((byte) 1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveByteAsByte() {
		final boolean actual = Match.caze((Byte b) -> true).caze((byte b) -> false).apply((byte) 1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchByteAsPrimitiveByte() {
		final boolean actual = Match.caze((byte b) -> true).caze((Byte b) -> false).apply(new Byte((byte) 1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchByte() {
		final boolean actual = Match.caze((Byte b) -> true).caze((byte b) -> false).apply(new Byte((byte) 1));
		assertThat(actual).isTrue();
	}

	// char / Character

	@Test
	public void shouldMatchPrimitiveChar() {
		final boolean actual = Match.caze((char c) -> true).caze((Character c) -> false).apply('#');
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveCharAsCharacter() {
		final boolean actual = Match.caze((Character c) -> true).caze((char c) -> false).apply('#');
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchCharacterAsPrimitiveChar() {
		final boolean actual = Match.caze((char c) -> true).caze((Character c) -> false).apply(Character.valueOf('#'));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchCharacter() {
		final boolean actual = Match.caze((Character c) -> true).caze((char c) -> false).apply(Character.valueOf('#'));
		assertThat(actual).isTrue();
	}

	// double / Double

	@Test
	public void shouldMatchPrimitiveDouble() {
		final boolean actual = Match.caze((double d) -> true).caze((Double d) -> false).apply((double) 1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveDoubleAsDouble() {
		final boolean actual = Match.caze((Double d) -> true).caze((double d) -> false).apply((double) 1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchDoubleAsPrimitiveDouble() {
		final boolean actual = Match.caze((double d) -> true).caze((Double d) -> false).apply(new Double(1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchDouble() {
		final boolean actual = Match.caze((Double d) -> true).caze((double d) -> false).apply(new Double(1));
		assertThat(actual).isTrue();
	}

	// float / Float

	@Test
	public void shouldMatchPrimitiveFloat() {
		final boolean actual = Match.caze((float f) -> true).caze((Float f) -> false).apply((float) 1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveFloatAsFloat() {
		final boolean actual = Match.caze((Float f) -> true).caze((float f) -> false).apply((float) 1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchFloatAsPrimitiveFloat() {
		final boolean actual = Match.caze((float f) -> true).caze((Float f) -> false).apply(new Float(1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchFloat() {
		final boolean actual = Match.caze((Float f) -> true).caze((float f) -> false).apply(new Float(1));
		assertThat(actual).isTrue();
	}

	// int / Integer

	@Test
	public void shouldMatchPrimitiveInt() {
		final boolean actual = Match.caze((int i) -> true).caze((Integer i) -> false).apply(1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveIntAsInteger() {
		final boolean actual = Match.caze((Integer i) -> true).caze((int i) -> false).apply(1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchIntegerAsPrimitiveInt() {
		final boolean actual = Match.caze((int i) -> true).caze((Integer i) -> false).apply(new Integer(1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchInteger() {
		final boolean actual = Match.caze((Integer i) -> true).caze((int i) -> false).apply(new Integer(1));
		assertThat(actual).isTrue();
	}

	// long / Long

	@Test
	public void shouldMatchPrimitiveLong() {
		final boolean actual = Match.caze((long l) -> true).caze((Long l) -> false).apply(1L);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveLongAsLong() {
		final boolean actual = Match.caze((Long l) -> true).caze((long l) -> false).apply(1L);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchLongAsPrimitiveLong() {
		final boolean actual = Match.caze((long l) -> true).caze((Long l) -> false).apply(new Long(1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchLong() {
		final boolean actual = Match.caze((Long l) -> true).caze((long l) -> false).apply(new Long(1));
		assertThat(actual).isTrue();
	}

	// short / Short

	@Test
	public void shouldMatchPrimitiveShort() {
		final boolean actual = Match.caze((short s) -> true).caze((Short s) -> false).apply((short) 1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveShortAsShort() {
		final boolean actual = Match.caze((Short s) -> true).caze((short s) -> false).apply((short) 1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchShortAsPrimitiveShort() {
		final boolean actual = Match.caze((short s) -> true).caze((Short s) -> false).apply(new Short((short) 1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchShort() {
		final boolean actual = Match.caze((Short s) -> true).caze((short s) -> false).apply(new Short((short) 1));
		assertThat(actual).isTrue();
	}

	// -- matching prototypes

	@Test
	public void shouldMatchPrimitiveBooleanValueAndApplyBooleanFunction() {
		final int actual = Match.caze(true, b -> 1).caze(Boolean.TRUE, b -> 2).apply(true);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchPrimitiveBooleanValueAsBooleanAndApplyBooleanFunction() {
		final int actual = Match.caze(Boolean.TRUE, b -> 1).caze(true, b -> 2).apply(true);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchByValuesUsingFunction() {
		final int actual = Match.caze("1", (String s) -> 1).apply("1");
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchByValueOnMultipleCases() {
		final int actual = Match.caze("1", o -> 1).caze("2", o -> 2).caze("3", o -> 3).apply("2");
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldCompileObjectIntegerPrototypeCase() {
		// This does *not* compile: new Match.Builder<>().caze(1, (int i) -> i);
		// Use this instead: Match.Builder<>().caze(1, i -> i);
		new Match.Builder<>().caze(1, (Integer i) -> i);
	}

	@Test
	public void shouldCompileUnqualifiedIntegerPrototypeCase() {
		new Match.Builder<>().caze(1, i -> i);
	}

	// -- matching arrays

	@Test
	public void shouldMatchBooleanArray() {
		final int actual = new Match.Builder<Integer>().caze((boolean[] b) -> 1).build().apply(new boolean[] { true });
		assertThat(actual).isEqualTo(1);
	}

	// -- return type of match

	@Test
	public void shouldAllowCommonReturnTypeUsingBuilder() {
		final Match<Number> toNumber = new Match.Builder<Number>()
				.caze((Integer i) -> i)
				.caze((String s) -> new BigDecimal(s))
				.build();
		final Number number = toNumber.apply("1.0E10");
		assertThat(number).isEqualTo(new BigDecimal("1.0E10"));
	}

	@Test
	public void shouldAllowCommonReturnTypeUsingBuilderAndPrototype() {
		final Match<Number> toNumber = new Match.Builder<Number>()
				.caze(1, (Integer i) -> i)
				.caze("1", (String s) -> new BigDecimal(s))
				.build();
		final Number number = toNumber.apply("1");
		assertThat(number).isEqualTo(new BigDecimal("1"));
	}

	@Test
	public void shouldAllowCommonReturnTypeUsingMatchs() {
		final Match<Number> toNumber = Match
				.<Number> caze((Integer i) -> i)
				.caze((String s) -> new BigDecimal(s))
				.build();
		final Number number = toNumber.apply("1");
		assertThat(number).isEqualTo(new BigDecimal("1"));
	}

	@Test
	public void shouldAllowCommonReturnTypeUsingMatchsWithPrototype() {
		final Match<Number> toNumber = Match
				.<Integer, Number> caze(1, (Integer i) -> i)
				.caze("1", (String s) -> new BigDecimal(s))
				.build();
		final Number number = toNumber.apply("1");
		assertThat(number).isEqualTo(new BigDecimal("1"));
	}

	// -- lambda type

	@Test
	public void shouldMatchLambdaConsideringTypeHierarchy() {
		final SpecialFunction lambda = i -> String.valueOf(i);
		final String actual = Match
				.caze((SameSignatureAsSpecialFunction f) -> f.apply(1))
				.caze((Function<Integer, String> f) -> f.apply(2))
				.apply(lambda);
		assertThat(actual).isEqualTo("2");
	}

	@FunctionalInterface
	static interface SpecialFunction extends Function<Integer, String> {
		@Override
		String apply(Integer i);
	}

	@FunctionalInterface
	static interface SameSignatureAsSpecialFunction extends Function<Integer, String> {
		@Override
		String apply(Integer i);
	}
}