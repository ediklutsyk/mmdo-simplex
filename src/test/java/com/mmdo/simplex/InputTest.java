package com.mmdo.simplex;

import com.mmdo.simplex.DTO.Equation;
import com.mmdo.simplex.DTO.SAE;
import com.mmdo.simplex.utils.InputUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mmdo.simplex.DTO.Condition.MIN;

public class InputTest {

    private InputUtils inputUtils;

    @Before
    public void setUp() {
        inputUtils = new InputUtils();
    }

    @Test
    public void testReadFromFile() {
        List<String> result = inputUtils.readFileFromResources("test_input.txt");
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(Arrays.asList("Some", "Lines", "Of", "Text"), result);
    }

    @Test
    public void testReadFromFileFailsWhenNoFile() {
        List<String> result = inputUtils.readFileFromResources("wrong_name.txt");
        Assert.assertNull(result);
    }

    @Test
    public void testParseFileWithComments() {
        List<String> result = inputUtils.parseFile("test_comment_input.txt");
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.stream().noneMatch(line->line.startsWith("//")));
        Assert.assertEquals("Text", result.get(0));
    }

    @Test
    public void testParseSAE() {
        SAE result = inputUtils.parseSAE("test_sae_input.txt");
        SAE expected = new SAE().setAmountOfUnknown(2)
                .setAmountOfEquations(3)
                .setCondition(MIN)
                .setFunction(Arrays.asList(11, 3))
                .setEquations(Arrays.asList(
                        new Equation().setA(Arrays.asList(2, -7)).setB(12).setSign('<'),
                        new Equation().setA(Arrays.asList(-1, 1)).setB(4).setSign('<'),
                        new Equation().setA(Arrays.asList(3, 4)).setB(30).setSign('<')
                ));
        Assert.assertEquals(expected, result);
    }


}