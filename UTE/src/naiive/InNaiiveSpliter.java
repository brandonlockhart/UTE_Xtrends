/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naiive;

//import InMem.*;
//import explelan.*;
//import explelan.View;
//import explelan.View.Metric;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import metadata.ColumnSorter;
import metadata.Metadata;

/**
 *
 * @author uqiibra2
 */
public class InNaiiveSpliter  {
    public InMView V;
    public Metadata MD;
    public String MaxSplitAtt1="";
    public String MaxSplitAttType1="";
    
    public void getMaxSplit(){
    
        String CurrentAtts=V.AggAtt+V.GrbyAtt;
        String MatAtt="";
       // String MatAttType="";
        double MAxDistinct=0;
        double  AttDistinctCount=0;
        try{
            Map<String, String> RemainCols = MD.getColumnAttributes();
         //   String type = rs.getString("TYPE_NAME");
                        
               for (Map.Entry<String, String> columnAttribute : RemainCols.entrySet()){
		
			String name = columnAttribute.getKey().toString();
                        String Type = columnAttribute.getValue();
                        if (!CurrentAtts.contains(name)){
                            if(V.WhrCond.length()>0)
                                AttDistinctCount=MD.getNumDistinctWhr(name," where "+ V.WhrCond);
                            else
                                AttDistinctCount=MD.getNumDistinctWhr(name, V.WhrCond);
                              //
                              if(AttDistinctCount>MAxDistinct) {
                                  MatAtt=name; 
                                  MAxDistinct=AttDistinctCount;
                                 if (Type.toLowerCase().startsWith("varchar"))
                                this.MaxSplitAttType1="C";
                                else
                                this.MaxSplitAttType1="R";
                              }
                        }
			
                        //
                       
                        
		}
             System.out.println("Max Spllitting Att:"+MatAtt);
             this.MaxSplitAtt1=MatAtt;
             
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public DefaultTableModel NaiiveSplitting (){
   
       DefaultTableModel AllPredicates=new DefaultTableModel();
         AllPredicates.addColumn("PredName");
         AllPredicates.addColumn("PredicateWehe");
//         AllPredicates.addColumn("NoRecods");
//         AllPredicates.addColumn("ValueafterDel");
//         AllPredicates.addColumn("Influence");
//         AllPredicates.addColumn("PU2-DistanceCorr");
         AllPredicates.addColumn("PU1-L2 norm");
         
         int PredCount=AllPredicates.getRowCount();
         
        try{
               // String AttrType=this.MaxSplitAttType;
               
                    if(V.WhrCond.length()>0)
                           V.WhrCond= " where "+ V.WhrCond;
                    
                    String CurrentAtts=V.AggAtt+V.GrbyAtt+V.WhrCond;
                    //Loop to premute each attribute 
                   // V.TableColumns=V.GetTableColumns();
                    Combination AttCatPremute= new Combination();
                                AttCatPremute.NVView=V;
                                AttCatPremute.AllPredicates=AllPredicates;
                                 
                    for (int i=0;i<V.TableColumns.getRowCount();i++){
                    
                            String AttributeName=V.TableColumns.getValueAt(i, 0).toString();
                            String AttributeType=V.TableColumns.getValueAt(i, 1).toString();
                          // if(! AttributeName.toLowerCase().contains(CurrentAtts.toLowerCase())){ 
                            if(! CurrentAtts.toLowerCase().contains(AttributeName.toLowerCase()))
                            // if varchar
                            if(AttributeType.toLowerCase().contains("char")){
                                // Getting Ditinct 
                                Object[] elements= V.GetDistinctCatData( AttributeName);
                                // Premute Ditinct 
                                
                                 System.out.println("Permuting All Predicate for: "+AttributeName+"\t"+"Data type: "+AttributeType);
                                 
                                 AttCatPremute.Strat(elements, AttributeName);
                                 
                                 System.out.println("--------------------------------------------------");
                                 System.out.println("No of Predicates for Attribute:"+AttributeName+" = "+(AllPredicates.getRowCount()-PredCount));
                                 System.out.println("--------------------------------------------------");
                                 PredCount=AllPredicates.getRowCount();
                                 
                            }
                            else
                            {
                                double MinVal=MD.getMin(AttributeName, V.WhrCond);
                                double MaxVal=MD.getMax(AttributeName, V.WhrCond);;
                                double Size=V.Split_ratio  *(MaxVal-MinVal) ; /// equal quater Partitions
                                System.out.println("Permuting All Predicate for: "+AttributeName+"\t"+"Data type: "+AttributeType);
                                System.out.println("Range Between  "+MinVal+ " AND "+ MaxVal +" on Size "+Size );
                                
                                
                                AttCatPremute.Numneric_Range(MinVal, MaxVal, Size, AttributeName);
                                
                                System.out.println("--------------------------------------------------");
                                System.out.println("No of Predicates for Attribute:"+AttributeName+" = "+(AllPredicates.getRowCount()-PredCount));
                                System.out.println("--------------------------------------------------");
                                PredCount=AllPredicates.getRowCount();
                            }
                             
                           // System.out.println("Att no: "+i);
                          }  
                    
                                System.out.println("---------------**************************-----------------------------------");
                                System.out.println("Total No of Predicates for SPlitting = "+(AllPredicates.getRowCount()));
                                System.out.println("---------------**************************-----------------------------------");
                                
                                
                                //Sort Neasest distance to target 
                                    Vector AllPredicates_data = AllPredicates.getDataVector();
                                    Collections.sort(AllPredicates_data, new ColumnSorter(2, true));
                                    
                                    V.AllSortedPredicates=AllPredicates;
                   // return AllPredicates;
                    }
        
        
        catch (Exception e){
            e.printStackTrace();
        }
        
         return AllPredicates;        
   
    }
    
}
    

