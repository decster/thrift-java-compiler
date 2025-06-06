package com.github.decster.gen;

import com.github.decster.ThriftAstBuilder;
import com.github.decster.ast.ConstNode;
import com.github.decster.ast.DefinitionNode;
import com.github.decster.ast.DocumentNode;
import com.github.decster.ast.EnumNode;
import com.github.decster.ast.ServiceNode;
import com.github.decster.ast.StructLikeNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.decster.gen.GeneratorTestUtil.assertEqualsLineByLine;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ConstantsGeneratorTest {
    @Test
    void testConstantsFilesCases() throws IOException {
        List<StructGeneratorFullFileTest.SingleFileCase> cases = StructGeneratorFullFileTest.loadSingleFileCasesFromResource("constants");
        assertFalse(cases.isEmpty(), "No test cases found");
        for (StructGeneratorFullFileTest.SingleFileCase testCase : cases) {
            testCase.test();
        }
    }
}
