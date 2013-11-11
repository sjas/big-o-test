/*
 * Copyright (C) 2013, Markus Sprunck <sprunck.markus@gmail.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.sw_engineering_candies.big_o_test;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.Table;
import com.sw_engineering_candies.big_o_test.fitter.RankedFittingFunctions;

public class BigOReports {

   /**
    * Use the platform independent line separator
    */
   private static final String NL = System.getProperty("line.separator");

   public static String getPolynomialDegree(final Table<Integer, String, Double> input) {
      // try to find all the fits
      final Double degree = BigOAnalyser.estimatePolynomialDegree(input);

      // print result
      final StringBuilder result = new StringBuilder(100);
      result.append("ESTIMATED-POLYNOMIAL-DEGREE").append(NL);
      result.append(String.format("%.4f", degree)).append(NL);
      return result.toString();
   }

   public static String getBestFunction(final Table<Integer, String, Double> input) {
      // try to find all the fits
      final RankedFittingFunctions functions = BigOAnalyser.calculateBestFittingFunctions(input);

      // return best fit
      return functions.get(functions.descendingKeySet().first());
   }

   public static String getBestFunctionsReport(final Table<Integer, String, Double> input) {
      // try to find best fits
      final RankedFittingFunctions functions = BigOAnalyser.calculateBestFittingFunctions(input);

      // add the function ordered by the R^2 value of the fits
      final StringBuilder result = new StringBuilder(1000);
      result.append("TYPE      \tR^2 (adjusted)\tFUNCTION").append(NL);
      for (final Double key : functions.descendingKeySet()) {
         result.append(functions.get(key)).append(NL);
      }
      result.append(NL);
      return result.toString();
   }

   public static String getDataReport(Table<Integer, String, Double> input) {
      final StringBuilder result = new StringBuilder(1000);

      // header of the table
      final Set<String> cols = input.columnKeySet();
      for (int i = 1; i < cols.size(); i++) {
         result.append("N" + i + "\t");
      }
      result.append("TIME").append(NL);

      // values of the table
      final SortedSet<Double> rows = new TreeSet<Double>();
      rows.addAll(input.column("N1").values());
      for (final Double value : rows) {
         Integer row = 0;
         for (final Integer index : input.column("N1").keySet()) {
            if (value.equals(input.get(index, "N1"))) {
               row = index;
               break;
            }
         }
         for (int col = 1; col < cols.size(); col++) {
            result.append(String.format("%.0f", input.get(row, "N" + col)) + "\t");
         }
         result.append(String.format("%.0f", input.get(row, "TIME"))).append(NL);
      }
      return result.toString();
   }

}
