/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naiive;

import explelan.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import metadata.ColumnSorter;
import metadata.Metadata;

/**
 *
 * @author uqiibra2
 */
public class BasicMerger {
    
    public Metadata Md;
    public InMView CurView;
    //DefaultTableModel PredMergedInfo=new DefaultTableModel ();
    
    double MergedSimilirty=0;
    
    
    public void Start() {
    
           CurView.NearestTargetSim= Double.parseDouble( CurView.AllSortedPredicates.getValueAt(0, 2).toString());
           CurView.TopPredicate=CurView.AllSortedPredicates.getValueAt(0, 1).toString();
           
           
        this.MergedSimilirty=CurView.NearestTargetSim;
        int i=1;
                while(this.MergedSimilirty<=CurView.NearestTargetSim && i<CurView.AllSortedPredicates.getRowCount()){
                
                    // Merge
                    InNaivePredicate MergedPredicate= new InNaivePredicate();
                    MergedPredicate.View=CurView;
                    MergedPredicate.M=CurView.VMD;
                    
                    String PredicateWHER=CurView.AllSortedPredicates.getValueAt(i, 1).toString();
                    InNaivePredicate TOPPredicate= FindFastPredicate(CurView.TopPredicate);
                    InNaivePredicate Predicate= FindFastPredicate(PredicateWHER);
                    
                   
                    
                    //Get right Condition i.e. remove duplications 
                    
                    MergedPredicate.PredicateWehe= MergedPredicate.ModifyMergedPedicates(TOPPredicate, Predicate);
                    MergedPredicate.PredName="Merged Predicate";
                    
                    if( !MergedPredicate.PredicateWehe.replaceAll(" ", "").equalsIgnoreCase(TOPPredicate.PredicateWehe.replaceAll(" ", ""))) {
                    MergedPredicate.ComputeTragetSimilirity();
                    this.MergedSimilirty=MergedPredicate.TargetSim;
                    System.out.println(MergedPredicate.PredName+"|\t"+MergedPredicate.PredicateWehe+"|\t"+MergedPredicate.TargetSim); 
                    
                             if(MergedPredicate.TargetSim<=CurView.NearestTargetSim)
                                    {
                                        CurView.TopPredicate=MergedPredicate.PredicateWehe;
                                        CurView.NearestTargetSim=MergedPredicate.TargetSim;

                                      

                                    }
                             
                               // Store New Merged Predicate;
                                        CurView.ViewPredicates.add(MergedPredicate);
                                        Vector PredStatInf= new Vector();
                                                        PredStatInf.add(MergedPredicate.PredName);
                                                        PredStatInf.add(MergedPredicate.PredicateWehe);
                //                                        PredStatInf.add(P.NoRecods);
                //                                        PredStatInf.add(P.ValueAfterDel);
                //                                        PredStatInf.add(P.Influence);
                //                                        PredStatInf.add(P.InternalSim);
                                                        PredStatInf.add(MergedPredicate.TargetSim);
                                                        
                                        CurView.AllSortedPredicates.addRow(PredStatInf);
                    }
//                    else
//                        System.out.println("ALready Merged Before !!!!!!!");
                    i++;
                    
                    
                }
            
                    //Sort Neasest distance to target 
                                    Vector AllPredicates_data = CurView.AllSortedPredicates.getDataVector();
                                    Collections.sort(AllPredicates_data, new ColumnSorter(2, true));
    }
    
    
    public boolean IsMergedBefore(String Predicate){
        boolean f=false;
        
        for(int i=0;i<this.CurView.AllSortedPredicates.getRowCount();i++){
            String MergedP=this.CurView.AllSortedPredicates.getValueAt(i, 1).toString();
            if(MergedP.contains(Predicate)){
                return f=true;
            }
        }
    return f;
    }
    
   
    public InNaivePredicate FindFastPredicate(String PredName){
    
        InNaivePredicate Predicate=new InNaivePredicate();
        for(InNaivePredicate NaivePredicate : CurView.ViewPredicates){
            
            String CurPredName=NaivePredicate.PredicateWehe.toString();
            
            if(CurPredName.equalsIgnoreCase(PredName))
            {
                Predicate=NaivePredicate;
                return NaivePredicate;
            }
        }
            return Predicate;
    }
    
    
    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
    public <T> int intersection_Count(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
            int i=0;
        for (T t : list1) {
            if(list2.contains(t)) {
               // list.add(t);
                i=i+1;
            }
        }

        return i;
    }
    
    public <T> List<T> union(List<T> list1, List<T> list2) {
         Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);

      
    }
    
    public String getBestAttrMergCluase( String Attr,List<String>  Merged_results){
       //int i=0;
       
        String Clause=" and "+Attr+" not in (";
        for (String temp : Merged_results) {
            Clause=Clause+"'"+temp+"',";
			//System.out.println(temp);
		}
        return Clause=Clause.substring(0,Clause.length()-1)+")";
    }
    
    public String ReversePredicate(String Predicate){
        Predicate= Predicate.replaceAll("<>", " = ");
        Predicate=Predicate.replaceAll(" not ", " ");
        return Predicate;
    }
    
    
}
