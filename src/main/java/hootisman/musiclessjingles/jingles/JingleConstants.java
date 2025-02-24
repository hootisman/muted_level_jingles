package hootisman.musiclessjingles.jingles;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JingleConstants {
    static final Set<Integer> ATTACK_UNLOCKS =
            Stream.of(5, 10, 15, 20, 30, 40, 42, 50, 55, 60, 65, 70, 75, 77, 78, 80, 82, 99).collect(Collectors.toUnmodifiableSet());

    static final Set<Integer> COOKING_UNLOCKS =
            Stream.of(5, 10, 15, 20, 30, 40, 42, 50, 55, 60, 65, 70, 75, 77, 78, 80, 82, 99).collect(Collectors.toUnmodifiableSet());
}
