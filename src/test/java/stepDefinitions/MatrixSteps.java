package stepDefinitions;

import com.example.exception.NoSquareException;
import com.example.model.Matrix;
import com.example.service.MatrixMathematics;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class MatrixSteps {

    private double det;
    private double[][] transposeMatrix;
    private double[][] inverseMatrix;
    private double[][] cofactorMatrix;
    private Matrix mat;

    @Given("I have A Matrix")
    public void iHaveAMatrix() {
        mat = new Matrix();
    }

    @When("I compute determinant of")
    public void iComputeDeterminantOf(DataTable table) throws NoSquareException {
        double[][] data = new double[3][3];
        List<Map<String, Double>> rows = table.asMaps(String.class, Double.class);
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Double> row = rows.get(i);
            data[i][0] = row.get("col1");
            data[i][1] = row.get("col2");
            data[i][2] = row.get("col3");
        }
        mat.setData(data);
        det = MatrixMathematics.determinant(mat);
    }

    @Then("The result of determinant is {double}")
    public void iFindAsDeterminantResult(double expected) {
        Assert.assertEquals(expected, det, 0);
    }

    @When("I compute transpose of")
    public void iComputeTransposeOf(DataTable table) {
        double[][] data = new double[3][2];
        List<Map<String, Double>> rows = table.asMaps(String.class, Double.class);
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Double> row = rows.get(i);
            data[i][0] = row.get("col1");
            data[i][1] = row.get("col2");
        }
        mat.setData(data);
        transposeMatrix = MatrixMathematics.transpose(mat).getValues();
    }

    @Then("The result of transpose is")
    public void iFindAsTransposeResult(DataTable table) {
        double[][] expected = new double[2][3];
        List<Map<String, Double>> rows = table.asMaps(String.class, Double.class);
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Double> row = rows.get(i);
            expected[i][0] = row.get("col1");
            expected[i][1] = row.get("col2");
            expected[i][2] = row.get("col3");
        }
        assertArrayEquals(expected, transposeMatrix);
    }

    @When("I compute inverse of")
    public void iComputeInverseOf(DataTable table) throws NoSquareException {
        double[][] data = new double[2][2];
        List<Map<String, Double>> rows = table.asMaps(String.class, Double.class);
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Double> row = rows.get(i);
            data[i][0] = row.get("col1");
            data[i][1] = row.get("col2");
        }
        mat.setData(data);
        inverseMatrix = MatrixMathematics.inverse(mat).getValues();
    }

    @Then("The result of inverse is")
    public void iFindAsInverseResult(DataTable table) {
        double[][] expected = new double[2][2];
        List<Map<String, Double>> rows = table.asMaps(String.class, Double.class);
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Double> row = rows.get(i);
            expected[i][0] = row.get("col1");
            expected[i][1] = row.get("col2");
        }
        assertArrayEquals(expected, inverseMatrix);
    }

    @When("I compute cofactor of")
    public void iComputeCofactorOf(DataTable table) throws NoSquareException {
        double[][] data = new double[2][2];
        List<Map<String, Double>> rows = table.asMaps(String.class, Double.class);
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Double> row = rows.get(i);
            data[i][0] = row.get("col1");
            data[i][1] = row.get("col2");
        }
        mat.setData(data);
        cofactorMatrix = MatrixMathematics.cofactor(mat).getValues();
    }

    @Then("The result of cofactor is")
    public void iFindAsCofactoreResult(DataTable table) {
        double[][] expected = new double[2][2];
        List<Map<String, Double>> rows = table.asMaps(String.class, Double.class);
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Double> row = rows.get(i);
            expected[i][0] = row.get("col1");
            expected[i][1] = row.get("col2");
        }
        assertArrayEquals(expected, cofactorMatrix);
    }
}

