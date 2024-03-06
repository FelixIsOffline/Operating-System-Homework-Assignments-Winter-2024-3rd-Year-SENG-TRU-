/*
 Felix Beauchemin-Berthelot (T00684599)
 Date: March 6th 2024
 Description: This is following the specifications for the assignment 3 for COMP 3411
 */

import java.util.Scanner;

public class MultiThreadedMatrixMultiplication {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Get the size of the matrix from the user input
        System.out.print("Enter the number of rows for matrix A: ");
        int rowsA = scan.nextInt();
        System.out.print("Enter the number of columns for matrix A: ");
        int colsA = scan.nextInt();
        System.out.print("Enter the number of rows for matrix B: ");
        int rowsB = scan.nextInt();
        System.out.print("Enter the number of columns for matrix B: ");
        int colsB = scan.nextInt();

        // Check if the multiplication of matrices is possible (colA == rowB && colB = rowA)
        if (colsA != rowsB || colsB != rowsA) {
            System.err.println("Error: Incorrect size for matrices input, impossible matrix multiplication");
            scan.close();
            return;
        }

        // Initialize A and B matrices with the user input
        int[][] matrixA = new int[rowsA][colsA];
        int[][] matrixB = new int[rowsB][colsB];

        System.out.println("Enter the elements for matrix A:");
        initializeMatrix(scan, matrixA);
        System.out.println("Enter the elements for matrix B:");
        initializeMatrix(scan, matrixB);

        // Matrix multiplication with the multiple threads
        int[][] ans = new int[rowsA][colsB];
        MatrixMultiplication(matrixA, matrixB, ans);

        // Print matrixA and matrixB as well as the answer of their product
        System.out.println("Matrix A:");
        printMatrix(matrixA);
        System.out.println("Matrix B:");
        printMatrix(matrixB);
        System.out.println("Answer:");
        printMatrix(ans);

        scan.close();
    }

    // Method used to initialize the matrices by asking for the user input
    private static void initializeMatrix(Scanner scan, int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print("Enter the element at row " + (i + 1) + ", column " + (j + 1) + ": ");
                matrix[i][j] = scan.nextInt();
            }
        }
    }

    // Method used to do the matrix multiplication using the multithreading concept
    private static void MatrixMultiplication(int[][] matrixA, int[][] matrixB, int[][] result) {
        Thread[] threads = new Thread[result.length * result[0].length];
        int threadIndex = 0;
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                threads[threadIndex] = new Thread(new MatrixMultiplicationThread(matrixA, matrixB, result, i, j));
                threads[threadIndex].start();
                threadIndex++;
            }
        }

        // Wait for all the threads to finish their execution
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method used to print the matrices
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

// Class that defines the behaviour of a thread and it performing the matrix multiplication
class MatrixMultiplicationThread implements Runnable {
    private final int[][] matrixA;
    private final int[][] matrixB;
    private final int[][] result;
    private final int row;
    private final int col;

    public MatrixMultiplicationThread(int[][] matrixA, int[][] matrixB, int[][] result, int row, int col) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.result = result;
        this.row = row;
        this.col = col;
    }

// Overriding the run() function, and defining the behaviour of the calculation within the thread when the thread is started
    @Override
    public void run() {
        int sum = 0;
        for (int k = 0; k < matrixA[0].length; k++) {
            sum += matrixA[row][k] * matrixB[k][col];
        }
        result[row][col] = sum;
    }
}
