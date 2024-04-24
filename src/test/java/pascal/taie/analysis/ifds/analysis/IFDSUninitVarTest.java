package pascal.taie.analysis.ifds.analysis;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pascal.taie.analysis.Tests;

public class IFDSUninitVarTest {

    private static final String CLASS_PATH = "src/test/resources/ifds/uninitvar";

    void testIFDSUV(String mainClass) {
        Tests.testMain(mainClass, CLASS_PATH, IFDSUninitializedVariables.ID,
                "-a", "cg=algorithm:cha", "-a", "icfg=dump:true" // <-- uncomment this code if you want
                // to output ICFGs for the test cases
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Example"
    })
    void test(String mainClass) {
        testIFDSUV(mainClass);
    }

}
