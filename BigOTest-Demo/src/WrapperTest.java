import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.BigOAnalyser;
import com.sw_engineering_candies.big_o_test.BigOAssert;
import com.sw_engineering_candies.big_o_test.BigOParameter;
import com.sw_engineering_candies.big_o_test.BigOReports;

public class WrapperTest {

   public void sortWrapper(@BigOParameter List<Long> values) {
      Collections.sort(values);
   }

   @Test
   public void simpleSortWrapper_RunJavaCollections_DetectPowerLaw() {

      // ARRANGE
      final BigOAnalyser boa = new BigOAnalyser();
      final WrapperTest sut = (WrapperTest) boa.createProxy(WrapperTest.class);
      boa.deactivate();                                                                                         // measurement is deactivated
      sut.sortWrapper(createSortInput(1024));        // give JIT compiler the chance to optimize
      boa.activate();                                                                                                // measurement is active

      // ACT
      for (int x = 256 * 1024; x >= 256; x /= 2) {
         sut.sortWrapper(createSortInput(x));
      }
      traceReport(boa, "sortWrapper");

      // ASSERT
      BigOAssert.assertPowerLaw(boa, "sortWrapper");
   }

   private static List<Long> createSortInput(int size) {
      final List<Long> result = new ArrayList<Long>(size);
      for (int i = 0; i < size; i++) {
         result.add(Math.round(Long.MAX_VALUE * Math.random()));
      }
      return result;
   }

   private static void traceReport(final BigOAnalyser boa, String method) {
      System.out.println("----------------------------------------");
      System.out.println("WrapperTest");
      final Table<Integer, String, Double> resultTable = boa.getResultTableChecked(method);
      System.out.println(BigOReports.caclulateBestFunctionsTable(resultTable));
      System.out.println(BigOReports.createDataReport(resultTable));
   }

}
