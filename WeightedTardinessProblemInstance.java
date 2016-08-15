import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.io.PrintWriter;
/**
 *
 * Copyright 2003, 2007, 2014, 2015, 2016 Vincent A. Cicirello
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *
 * Weighted Tardiness Scheduling with Sequence-Dependent Setups
 *
 * Problem Instance Generator
 *
 * Instance generator for the weighted tardiness scheduling problem with sequence-dependent
 * setups.
 * 
 * For information on the problem, including relevant publications see: http://www.cicirello.org/datasets/wtsds/ 
 *
 * On that page, you'll find a description of this generator, a benchmark problem set of instances of this problem, and copies of the publications where the
 * problem set and generator orignated, along with papers describing various algorithms for the problem.
 * 
 * A set of benchmark instances widely used by researchers for this scheduling problem was generated with this generator and 
 * can be found in the Harvard Dataverse: http://dx.doi.org/10.7910/DVN/VHA0VQ
 *
 *
 * @author (Vincent A. Cicirello) 
 * @version (08.11.2016)
 */
public class WeightedTardinessProblemInstance
{
    /**
     * Generates a random instance of the weighted tardiness scheduling problem with sequence-dependent setups.
     * @param tau the average duedate tightness factor; value between 0 and 1; the higher the value, the tighter 
     * the duedates
     * @param R the average duedate range factor; value between 0 and 1
     * @param eta the setup time severity factor; value between 0 and 1; the higher the value, the more setup times
     * play a role in the problem
     * @param N the number of jobs
     * @param seed random number seed
     */
    public WeightedTardinessProblemInstance(double tau, double R, double eta, int N, int seed) {
        
        int pave = 100;
        int save = (int)Math.round(eta * pave);
        double beta = 1.0 / (1.0 + Math.exp(1.094913175 - 1971.625259 / (1.0 + Math.exp(7.168150953 + 0.040112027 * N)) - 8.124363714 / (1.0 + Math.exp(-10.58867025 + 2.400027877 * N)) )); 
        int cmax = (int)Math.round(N * (pave + beta * save));
        double dave = (1.0 - tau) * cmax;
        
        duedates = new int[N];
        weights = new int[N];
        ptimes = new int[N];
        stimes = new int[N+1][N];
        
        Random gen = new Random(seed);
        
        for (int i = 0; i < N; i++) {
            ptimes[i] = 50 + gen.nextInt(101);   
            weights[i] = gen.nextInt(11);
            for (int j = 0; j <= N; j++) {
                stimes[j][i] = gen.nextInt(2 * save + 1);
            }
            if (gen.nextDouble() < tau) {
                duedates[i] = (int)Math.round(dave * (1 - R) + gen.nextInt((int)Math.round(R * dave) + 1));   
            } else {
                duedates[i] = (int)Math.round(dave + gen.nextInt(1 + (int)Math.round((cmax - dave) * R)));
            }
        }
        
   
    }

    /**
     * Constructor.
     * @param filename file with the problem instance
     */
    public WeightedTardinessProblemInstance(String filename) throws IOException {
        FileReader reader = new FileReader(filename);
        Scanner in = new Scanner(reader);
        
        while (!in.next().equals("Size:"));
        int n = in.nextInt();
        assert(n>0);
        duedates = new int[n];
        weights = new int[n];
        ptimes = new int[n];
        stimes = new int[n+1][n];
        while (!in.nextLine().equals("Process Times:"));
        for (int i = 0; i < n; i++) {
            ptimes[i] = in.nextInt();   
        }
        while (!in.hasNextInt()) {
            in.next();
        }
        for (int i = 0; i < n; i++) {
            weights[i] = in.nextInt();   
        }
        while (!in.hasNextInt()) {
            in.next();
        }
        for (int i = 0; i < n; i++) {
            duedates[i] = in.nextInt();   
        }
        while (!in.hasNextInt()) {
            in.next();
        }
        while (in.hasNextInt()) {
            int i = in.nextInt();
            int j = in.nextInt();
            int setup = in.nextInt();
            if (i == -1) i = n;
            stimes[i][j] = setup;
        }
        
        in.close();
        reader.close();
    }
    
    

    /**
     * Evaluates a schedule.  NOTE: does not verify if valid permutation.  Computes weighted tardiness
     * of the schedule represented by the permutation.
     * @param perm a permutation of the N jobs
     */
    public int evaluate(int[] perm) {
        int currentTime = 0;
        int lastJob = duedates.length;
        int twt = 0;
        for (int i = 0; i < duedates.length; i++) {
            int job = perm[i];
            currentTime += stimes[lastJob][job];
            currentTime += ptimes[job];
            int tardiness = Math.max(currentTime - duedates[job], 0);
            twt += weights[job] * tardiness;
            lastJob = job;
        }
        return twt;
    }
    
    
    
    
    /**
     * Returns the number of jobs in the problem instance.
     */
    public int getNumberOfJobs()
    {
        return duedates.length;   
    }
    
    /**
     * Gets duedate of job i.  Returns -1 if invalid job index.
     */
    public int getDuedate(int i)
    {
        if (i < duedates.length) {
            return duedates[i];   
        } else return -1;
    }
    
    /**
     * Gets weight of job i.  Returns -1 if invalid job index.
     */
    public int getWeight(int i)
    {
        if (i < duedates.length) {
            return weights[i];   
        } else return -1;
    }
    
    /**
     * Gets process time of job i.  Returns -1 if invalid job index.
     */
    public int getPTime(int i)
    {
        if (i < duedates.length) {
            return ptimes[i];   
        } else return -1;
    }
    
    /**
     * Gets setup of job i if it follows j.  Returns -1 if invalid job index.
     */
    public int getSetup(int j, int i)
    {
        if (j < stimes.length && i < stimes[j].length) {
            return stimes[j][i];   
        } else return -1;
    }
    
    /**
     * Gets setup of job i if sequenced first on the machine.  Returns -1 if invalid job index.
     */
    public int getSetup(int i)
    {
        if (i < stimes.length) {
            int n = stimes.length;
            return stimes[n-1][i];   
        } else return -1;
    }
    
    
    private int[] duedates;
    private int[] weights;
    private int[] ptimes;
    private int[][] stimes;
}
