/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XTR_Fast;

//import InMem.*;
//import explelan.*;
//import explelan.View;
//import explelan.View.Metric;
//import naiive.*;
//import XTR_Basic.*;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.table.DefaultTableModel;
import metadata.ColumnSorter;
import metadata.Metadata;

/**
 *
 * @author uqiibra2
 */
public class XTR_FastSpliter  {
    public XTR_FastView V;
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
    
    public void Splitting (){
   
      // DefaultTableModel AllPredicates=new DefaultTableModel();
    
         int PredCount=V.AllSortedPredicates.getRowCount();
         
        try{
               // String AttrType=this.MaxSplitAttType;
               
                    
                    
                    String CurrentAtts=V.AggAtt+V.GrbyAtt+V.WhrCond;
                    
                    
                    //Spliting using Result ID 
                    
                    for (int i=0;i<V.lines.getRowCount();i++){
                    
                            // For each Result GROUP
                            String ResultID_Name=V.lines.getValueAt(i, 0).toString();
                            String Predicate=" "+V.GrbyAtt+"='"+ResultID_Name+"' ";
                            double ResultTargetVal=Double.parseDouble(V.lines.getValueAt(i, 2).toString());
                            double ResultCurrentVal=Double.parseDouble(V.lines.getValueAt(i, 1).toString());
                            
                            String Stmt="Select distinct "+V.MaxSpit_AttName+" from "+V.Table;   
                            
                                if(V.WhrCond.length()>0){
                                        V.WhrCond= " where "+ V.WhrCond;
                                        //Stmt=Stmt+ V.WhrCond+" and "+Predicate;
                                        Stmt= V.WhrCond+" and "+Predicate;
                                }
                                else
                                         Stmt=" where "+Predicate;
                                //Stmt=Stmt+" where "+Predicate;
                             
                                // Getting Ditinct Values
                                int Rows_no_inResId=V.VMD.getNumRowsWhre(Stmt);
                               // ResultSet AttrDistinctValues=V.VMD.ExecuteQryWithRS(Stmt);

                                
                                // Generting Predicates NOWooooooW.........
                                
                                System.out.println("Genering All Predicate for Result ID: "+ResultID_Name);
                                
                                ResultID_Split( Rows_no_inResId,  ResultID_Name,  ResultTargetVal,ResultCurrentVal);
                                
                                 System.out.println("--------------------------------------------------");
                                 System.out.println("No of Predicates: = "+(V.AllSortedPredicates.getRowCount()-PredCount));
                                 System.out.println("--------------------------------------------------");
                                 PredCount=V.AllSortedPredicates.getRowCount();
                                 
                                            
                               
                            
                             
                                
                    }// End While
                            
                                System.out.println("---------------**************************-----------------------------------");
                                System.out.println("Total No of Predicates for SPlitting = "+(V.AllSortedPredicates.getRowCount()));
                                System.out.println("---------------**************************-----------------------------------");
                                
                                
                                //Sort Neasest S to Traget target InternalSim
                                    Vector AllPredicates_data = V.AllSortedPredicates.getDataVector();
                                    Collections.sort(AllPredicates_data, new ColumnSorter(3, true));
                                    
                                   // AllPredicates=V.AllSortedPredicates;
                   // return AllPredicates;
                    }
        
        
        catch (Exception e){
            e.printStackTrace();
        }
        
       //  return V.AllSortedPredicates;        
   
    }
    
    
   
    
    
    public void ResultID_Split(int  Rows_no_inResId, String ResultID_Name, double ResultID_TargetVal,double ResultCurrentVal){
    
        
        String Pred="";
        DefaultTableModel ResultID_Pred=new DefaultTableModel();
        
                    ResultID_Pred.addColumn("PredName");
                    ResultID_Pred.addColumn("PredicateWehe");
                    ResultID_Pred.addColumn("InternalSim");
                    ResultID_Pred.addColumn("PU1-L2 norm");
            
        try{
            
            if(V.AllSortedPredicates.getRowCount()<1 )
            {
                    V.AllSortedPredicates.addColumn("PredName");
                    V.AllSortedPredicates.addColumn("PredicateWehe");
                    V.AllSortedPredicates.addColumn("InternalSim");
                    V.AllSortedPredicates.addColumn("PU1-L2 norm");
                    
                    
            
            }
            
            System.out.println("PredicateWhere |\t InternalSim  |\t TargetSim");
           // double count=0;
            for(int i=0;i<V.OrgData.getRowCount();i++){
                
                         
                         String Row_resltID=V.OrgData.getValueAt(i, 1).toString();
                         
                   if(Row_resltID.equalsIgnoreCase(ResultID_Name))      {
              //   while (AttrDistinctValues.next()) {
                     
                    // String Val=AttrDistinctValues.getString(1);
                      XTR_FastPredicate Predicate= new XTR_FastPredicate();
                         Predicate.View=V;
                         Predicate.M=V.VMD;
                         Pred=" and "+V.GrbyAtt+"='"+ResultID_Name+"' ";
                         String Row_ID=V.OrgData.getValueAt(i, 0).toString();
                         double Row_AggVal=Double.parseDouble( V.OrgData.getValueAt(i, 2).toString()) ;
                         
                         
                     // Stores Predicate Atteibure
                            Predicate.Store_PredAtts(V.GrbyAtt, "=", "'"+ResultID_Name+"'", "C");
                            
                     if(V.MaxSpit_AttType.toLowerCase().contains("char")){
                         
                          Pred=" and "+V.GrbyAtt+"='"+ResultID_Name+"' and "+V.MaxSpit_AttName+"<>'"+Row_ID+"'";
                          Predicate.Store_PredAtts(V.MaxSpit_AttName, "<>", "'"+Row_ID+"'", "C");
                     }
                     else{
                          Pred=" and "+V.GrbyAtt+"='"+ResultID_Name+"' and "+V.MaxSpit_AttName+"<>"+Row_ID+"";
                          Predicate.Store_PredAtts(V.MaxSpit_AttName, "<>", Row_ID, "R");
                         }
                         Predicate.PredName=Pred;
                         Predicate.PredicateWehe=Pred; 
                         Predicate.NoRecods=1;
                         if(V.AggFun.equalsIgnoreCase("sum")||V.AggFun.equalsIgnoreCase("avg")){
                                Predicate.InternalSim=Math.abs( ResultID_TargetVal-(((ResultCurrentVal*Rows_no_inResId)-Row_AggVal)/(Rows_no_inResId- Predicate.NoRecods))) ;
                                Predicate.PredSingleAggValue=Row_AggVal;
                                
                         }
                         else if(V.AggFun.equalsIgnoreCase("count")){
                                Predicate.InternalSim=Math.abs( ResultID_TargetVal-(ResultCurrentVal-1)) ;
                                Predicate.PredSingleAggValue=1;
                         }
                        
                         //Predicate.InternalSim=Math.abs( ResultID_TargetVal-Predicate.GetResultID_Value(ResultID_Name)) ;
                         
                        
                        // Store Predicate Information 
                            V.ViewPredicates.add(Predicate);
                             Vector PredStatInf= new Vector();
                                        PredStatInf.add(Predicate.PredName);
                                        PredStatInf.add(Predicate.PredicateWehe);
                                        PredStatInf.add(Predicate.InternalSim);
                                        PredStatInf.add(Predicate.TargetSim);
                         V.AllSortedPredicates.addRow(PredStatInf);
                         
                         ResultID_Pred.addRow(PredStatInf);
                         
                      System.out.println(Predicate.PredicateWehe+"|\t"+Predicate.InternalSim+"|\t"+Predicate.TargetSim);    
                     // count++;
                 }
        }
                 
             StartMerg( ResultID_Name,  ResultID_TargetVal,ResultCurrentVal,Rows_no_inResId,ResultID_Pred);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void StartMerg(String ResultID_Name, double ResultID_TargetVal,double ResultCurrentVal,double count,DefaultTableModel ResultID_Pred) {
    
        //Sort Neasest S to Internal target InternalSim
                                    Vector AllPredicates_data = ResultID_Pred.getDataVector();
                                    Collections.sort(AllPredicates_data, new ColumnSorter(2, true));
                
                                 String TopPredicate_where=ResultID_Pred.getValueAt(0, 1).toString();
                                    //double MergedSimilirty=Double.parseDouble(V.AllSortedPredicates.getValueAt(0, 2).toString());
                                    XTR_FastPredicate TOPPredicate= FindXTR_BasicPredicate(TopPredicate_where);
                                    double MergedSimilirty=TOPPredicate.InternalSim;
                                    double MaxMergedSimilirty= MergedSimilirty;
                                    //TOPPredicate.ComputeTragetSimilirity();
                                    
                if (Double.isNaN(TOPPredicate.TargetSim  ))
                    TOPPredicate.ComputeTragetSimilirity();
                
                
         //  V.NearestTargetSim= MergedSimilirty;
           
           
           
        
        int i=1;
                while(MergedSimilirty<=MaxMergedSimilirty && i<ResultID_Pred.getRowCount() ){
                
                    // Merge
                    XTR_FastPredicate MergedPredicate= new XTR_FastPredicate();
                    MergedPredicate.View=V;
                    MergedPredicate.M=V.VMD;
                    
                    String PredicateWHER=ResultID_Pred.getValueAt(i, 1).toString();
                    
                    XTR_FastPredicate Predicate= FindXTR_BasicPredicate(PredicateWHER);
                    
                   
                    
                    //Get right Condition i.e. remove duplications 
                    
                    MergedPredicate.PredicateWehe= MergedPredicate.ModifyMergedPedicates(TOPPredicate, Predicate);
                    MergedPredicate.NoRecods=TOPPredicate.NoRecods+Predicate.NoRecods;
                    MergedPredicate.PredSingleAggValue=TOPPredicate.PredSingleAggValue+Predicate.PredSingleAggValue;
                    
                    MergedPredicate.PredName="Internal Merged Predicate";
                    
                    if( !MergedPredicate.PredicateWehe.replaceAll(" ", "").equalsIgnoreCase(TOPPredicate.PredicateWehe.replaceAll(" ", ""))) {
                    
                        
                   // MergedSimilirty=MergedPredicate.TargetSim;
                    // Compute Internal Similirity 
                    //MergedPredicate.InternalSim=Math.abs( ResultID_TargetVal-MergedPredicate.GetResultID_Value(ResultID_Name)) ;
                    if(V.AggFun.equalsIgnoreCase("sum")||V.AggFun.equalsIgnoreCase("count"))
                        MergedPredicate.InternalSim=Math.abs( ResultID_TargetVal-(ResultCurrentVal-MergedPredicate.PredSingleAggValue)) ;
                    
                    else  if(V.AggFun.equalsIgnoreCase("avg"))
                        MergedPredicate.InternalSim=Math.abs( ResultID_TargetVal-(((ResultCurrentVal*count)-MergedPredicate.PredSingleAggValue)/(count-MergedPredicate.NoRecods))) ;
                    //System.out.println(MergedPredicate.PredicateWehe+"|\t"+MergedPredicate.InternalSim+"|\t"+MergedPredicate.TargetSim); 
                    
                             if(MergedPredicate.InternalSim<=MaxMergedSimilirty)
                                    {
//                                        V.TopPredicate=MergedPredicate.PredicateWehe;
//                                        V.NearestTargetSim=MergedPredicate.InternalSim;
                                        
                                        TOPPredicate=MergedPredicate;
                                        TopPredicate_where=MergedPredicate.PredicateWehe;
                                        MaxMergedSimilirty=MergedPredicate.InternalSim;
                                        
                                        MergedPredicate.ComputeTragetSimilirity();

                                      // Store Only Good Merged Predicate;
                                        V.ViewPredicates.add(MergedPredicate);
                                        Vector PredStatInf= new Vector();
                                                        PredStatInf.add(MergedPredicate.PredName);
                                                        PredStatInf.add(MergedPredicate.PredicateWehe);
                                                        PredStatInf.add(MergedPredicate.InternalSim);
                                                        PredStatInf.add(MergedPredicate.TargetSim);
                                        V.AllSortedPredicates.addRow(PredStatInf);
                                        System.out.println(MergedPredicate.PredicateWehe+"|\t"+MergedPredicate.InternalSim+"|\t"+MergedPredicate.TargetSim); 
                                    }
                             
                               
                //                                        PredStatInf.add(P.NoRecods);
                //                                        PredStatInf.add(P.ValueAfterDel);
                //                                        PredStatInf.add(P.Influence);
                //                                        PredStatInf.add(P.InternalSim);
                                                       
                    }
//                    else
//                        System.out.println("ALready Merged Before !!!!!!!");
                    i++;
                    
                    
                }
            
//                    //Sort Neasest distance to target 
//                                    Vector AllPredicates_data = V.AllSortedPredicates.getDataVector();
//                                    Collections.sort(AllPredicates_data, new ColumnSorter(2, true));
    }
    
    
     public XTR_FastPredicate FindXTR_BasicPredicate(String PredName){
    
        XTR_FastPredicate Predicate=new XTR_FastPredicate();
        for(XTR_FastPredicate NaivePredicate : V.ViewPredicates){
            
            String CurPredName=NaivePredicate.PredicateWehe.toString();
            
            if(CurPredName.equalsIgnoreCase(PredName))
            {
                Predicate=NaivePredicate;
                return NaivePredicate;
            }
        }
            return Predicate;
    }
}
    

