/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naiive;

//import explelan.Predicate;
import java.math.BigInteger;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author uqiibra2
 */



public class Combination {

	public  static InMView NVView;
	public  static DefaultTableModel AllPredicates= new DefaultTableModel();
	
	
	public static void combination(Object[]  elements, int K,String Att_name){

		// get the length of the array
		// e.g. for {'A','B','C','D'} => N = 4 
		int N = elements.length;
		
		if(K > N){
			System.out.println("Invalid input, K > N");
			return;
		}
		// calculate the possible combinations
		// e.g. c(4,2)
		//c(N,K);
		c_big(N,K);
		// get the combination by index 
		// e.g. 01 --> AB , 23 --> CD
		int combination[] = new int[K];
		
		// position of current index
		//  if (r = 1)				r*
		//	index ==>		0	|	1	|	2
		//	element ==>		A	|	B	|	C
		int r = 0;		
		int index = 0;
		
		while(r >= 0){
			// possible indexes for 1st position "r=0" are "0,1,2" --> "A,B,C"
			// possible indexes for 2nd position "r=1" are "1,2,3" --> "B,C,D"
			
			// for r = 0 ==> index < (4+ (0 - 2)) = 2
			if(index <= (N + (r - K))){
					combination[r] = index;
					
				// if we are at the last position print and increase the index
				if(r == K-1){

					//do something with the combination e.g. add to list or print
					print(combination, elements,Att_name);
					index++;				
				}
				else{
					// select index for next position
					index = combination[r]+1;
					r++;										
				}
			}
			else{
				r--;
				if(r > 0)
					index = combination[r]+1;
				else
					index = combination[0]+1;	
			}			
		}
	}
	

	
	public static int c(int n, int r){
		int nf=fact(n);
		int rf=fact(r);
		int nrf=fact(n-r);
		int npr=nf/nrf;
		int ncr=npr/rf; 
		
		//System.out.println("C("+n+","+r+") = "+ ncr);

		return ncr;
	}
        
        public static BigInteger c_big(int n, int r){
		//long nf=fact_big(n);
                BigInteger nf_big=calculateFactorial(n);
		//long rf=fact_big(r);
                BigInteger rf_big=calculateFactorial(r);
		//long nrf=fact_big(n-r);
                BigInteger nrf_big=calculateFactorial(n-r);
                
		//double npr=nf/nrf;
                BigInteger npr_big=nf_big.divide(nrf_big);
		//double ncr=npr/rf; 
		BigInteger ncr_big=npr_big.divide(rf_big);
		//System.out.println("C("+n+","+r+") = "+ ncr);

		//return ncr;
                return ncr_big;
	}
	
	public static int fact(int n) 	{
		if(n == 0)
			return 1;
		else
			return n * fact(n-1);
	}
        
        public static long fact_big(int n) 	{
		if(n == 0)
			return 1;
		else
			return n * fact(n-1);
	}
        
        public static BigInteger calculateFactorial(int n) {

         
        BigInteger result = BigInteger.ONE;

        for (int i=1; i<=n; i++) {

            result = result.multiply(BigInteger.valueOf(i));
        }
    //    System.out.println(n + "! = " + result);
        return result;
    }

	
        
	public static void print(int[] combination, Object[] elements,String Att_name){

		String output = " and "+Att_name + " IN('";
		for(int z = 0 ; z < combination.length;z++){
			output += elements[combination[z]]+"','";
		}
                output=output.substring(0,output.lastIndexOf(","))+")";
                        
		//System.out.println(output);
                
                InNaivePredicate P= new InNaivePredicate();
                //P.View=
                P.View=NVView;
                                         P.M=NVView.VMD;
                                         P.PredName=output;
                                         P.PredicateWehe= output.replaceAll("IN", "NOT IN");
                                        // P.PredName= output.replaceAll("IN", "NOT IN");
                                         
                                         P.ComputeTragetSimilirity();
                                         NVView.ViewPredicates.add(P);
                                         
                                        // Storing......
                                        Vector PredStatInf= new Vector();
                                        PredStatInf.add(P.PredName);
                                        PredStatInf.add(P.PredicateWehe);
//                                        PredStatInf.add(P.NoRecods);
//                                        PredStatInf.add(P.ValueAfterDel);
//                                        PredStatInf.add(P.Influence);
//                                        PredStatInf.add(P.InternalSim);
                                        PredStatInf.add(P.TargetSim);
                                        AllPredicates.addRow(PredStatInf); 
                                        
                                        String PredAttValues=output.replaceAll("IN", "NOT IN");
                                        PredAttValues=PredAttValues.substring(PredAttValues.lastIndexOf("(")+1, PredAttValues.lastIndexOf(")"));
                                        P.Store_PredAtts(Att_name, "NOT IN", PredAttValues, "C");
                                        
                                    System.out.println(P.PredName+"|\t"+P.PredicateWehe+"|\t"+P.TargetSim);    
	}
        
        public  void Strat( Object[] elements,String Att_name){
            
            System.out.println("PredName |\t PredicateWhere  |\t TargetSim");
            for (int i=1; i<=elements.length; i++)
                combination(elements,i,Att_name);
        }
        public  void Numneric_Range (double min, double max , double size,String Att_name){
        
            //double max1=max;
            String output=" and "+Att_name+" BETWEEN ";
            for (double i=min; i<max; i=i+size){
                
            int t=1;
                for (double j=i+size; j<max+size; j=j+size){
                    
                    if (j>= max && t==1){
                       // =output+
                        output=output+i+" AND "+max;
                       // System.out.println(output +i+" AND "+max);
                        t++;
                            }
                    else{
                        output=output+i+" AND "+j;
                        //System.out.println(output+i+" AND "+j);
                    }
                    // Predicate Generation 
                    InNaivePredicate P= new InNaivePredicate();
                //P.View=
                P.View=NVView;
                                         P.M=NVView.VMD;
                                         P.PredName=output;
                                        // P.PredName= output.replaceAll("BETWEEN", "NOT BETWEEN");
                                         P.PredicateWehe= output.replaceAll("BETWEEN", "NOT BETWEEN");
                                         output=" and "+Att_name+" BETWEEN ";
                                         P.ComputeTragetSimilirity();
                                         NVView.ViewPredicates.add(P);
                                         
                                        // Storing......
                                        Vector PredStatInf= new Vector();
                                        PredStatInf.add(P.PredName);
                                        PredStatInf.add(P.PredicateWehe);
//                                        PredStatInf.add(P.NoRecods);
//                                        PredStatInf.add(P.ValueAfterDel);
//                                        PredStatInf.add(P.Influence);
//                                        PredStatInf.add(P.InternalSim);
                                        PredStatInf.add(P.TargetSim);
                                        String PredAttValues=P.PredicateWehe;
                                        PredAttValues=PredAttValues.substring(PredAttValues.lastIndexOf("BETWEEN")+7, PredAttValues.length());
                                        
                                        P.Store_PredAtts(Att_name, "NOT BETWEEN", PredAttValues, "R");
                                       
                                        
                                        AllPredicates.addRow(PredStatInf); 
                                        
                                    System.out.println(P.PredName+"|\t"+P.PredicateWehe+"|\t"+P.TargetSim);   
                    
                }
            }
        }
}