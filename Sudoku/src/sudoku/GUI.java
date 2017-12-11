package sudoku;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GUI implements ActionListener {
    JPanel cont,p,p1;
    static JFrame f;
    static JTextField[] tf = new JTextField[81];
    static JButton b1,b2,b3;
    static int x,y,tam = 9;
    static int[][] sudoku=new int[tam][tam];
    static double tiempo=0.00;
    static boolean flag=false;
    
    public GUI(){
        f = new JFrame("Resolución del Sudoku (Reducibilidad)");
        //f.setLocationRelativeTo(null);
        cont = new JPanel();
        cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));        
        p = new JPanel(new GridLayout(tam,tam));

        for(int i=0; i<tam;i++)
        {
            for(int j=0;j<tam;j++)
            {
                tf[i*tam + j] = new JTextField("");
                p.add(tf[i*tam + j]);
            }
        }
        
        p1 = new JPanel();        
        b1 = new JButton("Resolver Sudoku");
        b1.addActionListener(this);        
        p1.add(b1);        
        b2 = new JButton("Reiniciar Sudoku");
        b2.addActionListener(this);        
        p1.add(b2);
        b3 = new JButton("Llenar Sudoku");
        b2.addActionListener(this);        
        p1.add(b3);        
        cont.add(p);
        cont.add(p1);        
        f.add(cont);        
        f.pack();
        f.setVisible(true);
        f.setSize(500,410);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == b1)
        {
            solve();
            if(flag)
                b1.setText("¡Resuelto! Tiempo: "+tiempo+" seg.");
            else
                b1.setText("Resolver Sudoku");
        }
        if(e.getSource() == b2)
            reset();    
        if(e.getSource() == b3)
            makeSudoku();
    }
    
    
    private static void reset(){
        for(int i = 0 ; i < tam*tam ; i++ )
        {
            tf[i].setText("");
        }        
        b1.setText("Resolver Sudoku");        
        JOptionPane.showMessageDialog(f, "Se ha reiniciado el Sudoku.");
    }
    
    
    public static void main(String[] args){
        new GUI();
    }
    
    private static void solve(){
        long t1 = System.currentTimeMillis();        
        makeSudoku();        
        if( validate() )
        {
            if( solveSudoku() )            
                flag = true ;            
            else            
                JOptionPane.showMessageDialog(f,"¡Sudoku no válido! Intente de nuevo. Tiempo: "+tiempo+" seg.");            
        }
        else        
            JOptionPane.showMessageDialog(f,"¡Sudoku no válido! Intente de nuevo.");              
        tiempo = ( System.currentTimeMillis() - t1 )/1000.000 ;
    }
    
    private static void makeSudoku(){
        for(int i=0 ; i< tam ; i++ )
        {
            for(int j=0 ; j< tam ; j++ )
            {
                if( ( (tf[i*tam + j]).getText() ).equals("") )
                    sudoku[i][j] = 0 ;
                else
                    sudoku[i][j] = (int)( (tf[i*tam + j]).getText().charAt(0) ) - 48;
            }
        }
    }
    
    
    private static boolean solveSudoku(){
        int row = 0 , col = 0;
        boolean f = false;
        //find unassigned location
        for( row = 0 ; row < tam ; row++ )
        {
            for( col = 0 ; col < tam ; col++ )
            {
                if( sudoku[row][col] == 0 )
                {
                    f = true;
                    break;
                }
            }
            if( f == true )
                break;
        }
        
        if( f == false )     //if no unassigned
            return true ;
        
        for(int n = 1 ; n <= tam ; n++ )
        {
            if( isSafe(row,col,n) )
            {
                //make assignment
                sudoku[row][col] = n ;
                //print output
                (tf[(row)*tam + col]).setText(Integer.toString(n));
               
                //return if success
                if( solveSudoku() )
                    return true;
                //if fail , undo and try again
                sudoku[row][col] = 0 ;
            }
        }        
        //trigger backtracking
        return false ;
    }
        
    private static boolean validate(){
        for(int i=0 ; i<tam ; i++ )
        {
            for(int j=0 ; j<tam ; j++ )
            {
                if( sudoku[i][j] < 0 && sudoku[i][j] > tam )
                    return false ;                
                if( sudoku[i][j] != 0 && (usedInRow(i,j,sudoku[i][j]) || usedInCol(i,j,sudoku[i][j]) || usedInBox(i,j, sudoku[i][j]) ) )                
                    return false ;                
            }
        }        
        return true ;
    }
    
    
    private static boolean isSafe(int r , int c , int n){
        return ( !usedInRow(r,c,n) && !usedInCol(r,c,n) && !usedInBox(r,c,n) ) ;
    }
    
    
    private static boolean usedInRow(int r , int c, int n){ 
        for(int col=0 ; col<tam ; col++ )
        {
            if( col != c && sudoku[r][col] == n )            
                return true;            
        }                
        return false;
    }
    
    
    private static boolean usedInCol(int r,int c , int n){
        for(int row=0 ; row < tam ; row++ )
        {
            if( row != r && sudoku[row][c] == n )            
                return true;            
        }        
        return false;
    }
    
    
    private static boolean usedInBox(int r , int c , int n){
        int r_st = r-r%((int)Math.sqrt(tam)) ;
        int c_st = c-c%((int)Math.sqrt(tam)) ;        
        for(int i=0 ; i< (int)Math.sqrt(tam) ; i++ )
        {
            for(int j=0 ; j< (int)Math.sqrt(tam) ; j++ )
            {
                if( r_st+i != r && c_st+j != c && sudoku[r_st+i][c_st+j] == n )                
                    return true;                
            }
        }
        return false;
    }    
}