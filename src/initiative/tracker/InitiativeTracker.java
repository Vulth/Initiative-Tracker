/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package initiative.tracker;

import java.util.Scanner;
import java.io.*;
import java.util.Random;

/**
 *
 * @author Thomas
 */

class Combatant
{
    public Combatant(int newinit, String newname, int chealth, int mhealth)
    {
        initiative = newinit;
        name = newname;
        currenthealth = chealth;
        maxhealth = mhealth;
    }
    int initiative;
    String name;
    int currenthealth;
    int maxhealth;
}

class CombatArray
{
    public CombatArray()
    {
        fighters = new Combatant[30];
        totalfighters = 0;
        partylist = new String[10];
        totalpartymembers = 0;
    }
    Combatant[] fighters;
    int totalfighters;
    String[] partylist;
    int totalpartymembers;
}

public class InitiativeTracker {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    public static void main(String[] args) throws IOException 
    {
        CombatArray brawl = new CombatArray();
        Scanner input = new Scanner(System.in);
        String partyFileName = "";
        String enemyFileName = "";
        Random rand = new Random();
        
        partyFileName = getFileName(1); //Get the file with the party list
        try (BufferedReader br = new BufferedReader(new FileReader(partyFileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] statline = line.split(" ");
                StringBuilder newname = new StringBuilder();
                int healthDividerPos = 0;
                
                while(!statline[healthDividerPos].equals("|"))
                {
                    newname.append(statline[healthDividerPos]);
                    healthDividerPos++;
                    if (!statline[healthDividerPos].equals("|"))
                    {
                        newname.append(" ");
                    }
                }
                System.out.print("What is " + newname + "'s initiative? ");
                int newinit = Integer.parseInt(input.nextLine());
                
                int heroCurrentHealth = Integer.parseInt(statline[healthDividerPos + 1]);
                int heroMaximumHealth = Integer.parseInt(statline[healthDividerPos + 2]);
                
                Combatant newfighter = new Combatant (newinit, newname.toString(), heroCurrentHealth, heroMaximumHealth);
                brawl.fighters[brawl.totalfighters] = newfighter;
                brawl.totalfighters++;
                brawl.partylist[brawl.totalpartymembers] = newfighter.name;
                brawl.totalpartymembers++;
            }
        }
        
        enemyFileName = getFileName(0); //Get the file with the enemy list
                
        try (BufferedReader br = new BufferedReader(new FileReader(enemyFileName)))
        {
            String characterInfo;
            while ((characterInfo = br.readLine()) != null)
            {
                String[] statline = characterInfo.split(" ");
                int initmod = Integer.parseInt(statline[0]);
                int newinit = rand.nextInt(20) + initmod + 1;
                if (newinit < 1) //Character cannot have an initiative of less than 1
                {
                    newinit = 1;
                }
                StringBuilder newname = new StringBuilder();
                
                int healthDividerPos = 1;
                while(!statline[healthDividerPos].equals("|"))
                {
                    newname.append(statline[healthDividerPos]).append(" ");
                    healthDividerPos++;
                }
                int monsterCurrentHealth = Integer.parseInt(statline[healthDividerPos + 1]);
                int monsterMaximumHealth = Integer.parseInt(statline[healthDividerPos + 2]);
                
                Combatant newfighter = new Combatant (newinit, newname.toString(), monsterCurrentHealth, monsterMaximumHealth);
                brawl.fighters[brawl.totalfighters] = newfighter;
                brawl.totalfighters++;
            }
        }
        
        if (brawl.totalfighters > 0)
        {
            brawl = sortInitiative(brawl);
        }
        
        String userinput = "";
        while (!userinput.equals("done")) //Loop to get user input and apply commands to the list
        {
            printFighters(brawl);
            System.out.println();
            System.out.println("Available Commands");
            System.out.println("\"new\": Add a new fighter to the initiative order");
            System.out.println("\"hit\": Damage an existing fighter");
            System.out.println("\"heal\": Heal an existing fighter");
            System.out.println("\"done\": End the current fight");
            System.out.print("Enter a command: ");
            userinput = input.nextLine();
            System.out.println();
            if (userinput.equals("new"))
            {
                brawl = addNewFighter(brawl);
            }
            else if (userinput.equals("hit"))
            {
                brawl = damageFighter(brawl);
            }
            else if (userinput.equals("heal"))
            {
                brawl = healFighter(brawl);
            }
            else if (userinput.equals("done"))
            {
                savePartyStats(brawl, partyFileName);
            }
            else
            {
                System.out.println("That was not a valid command");
            }
            System.out.println();
             
        }
    } 

    /**
     *
     * @param brawl
     * @return
     */
    public static CombatArray sortInitiative(CombatArray brawl)
    {
        int maxinit = brawl.fighters[0].initiative;
        int maxinitpos = 0;
        for (int i = 0; i < brawl.totalfighters; i++)
        {
            maxinit = brawl.fighters[i].initiative;
            maxinitpos = i;
            for (int j = i; j < brawl.totalfighters; j++)
            {
                if (brawl.fighters[j].initiative > maxinit)
                {
                    maxinit = brawl.fighters[j].initiative;
                    maxinitpos = j;
                }
            }
            Combatant temp = brawl.fighters[i];
            brawl.fighters[i] = brawl.fighters[maxinitpos];
            brawl.fighters[maxinitpos] = temp;
        }
        
        return brawl;
    }
    
    public static void printFighters(CombatArray brawl)
    {
        for (int i = 0; i < brawl.totalfighters; i++)
        {
            if (brawl.fighters[i].currenthealth > 0)
            {
                System.out.println(brawl.fighters[i].initiative + " " + brawl.fighters[i].name + " " + brawl.fighters[i].currenthealth + "/" + brawl.fighters[i].maxhealth);
            }
        }
        System.out.println();
    }
    
    public static String getFileName(int party) 
    {
        String str = "";
        StringBuilder tempFileName = new StringBuilder();
        Scanner input = new Scanner(System.in);
        if (party == 0)
        {
            System.out.println("Type in the name of the file with the enemy list: ");
        }
        else
        {
            System.out.println("Type in the name of the file with the party list: ");
        }
        
        str = input.nextLine();
        tempFileName.append(str);
        tempFileName.append(".txt");
        
        return tempFileName.toString();
    }
    
    /**
     *
     * @param brawl
     * @return
     */
    public static CombatArray addNewFighter(CombatArray brawl)
    {
        System.out.println("Please type the initiative, name, \"|\", current health and maximum health of the new fighter: ");
        Scanner input = new Scanner(System.in);
        String characterInfo = input.nextLine();
        String[] statline = characterInfo.split(" ");
        int newinit = Integer.parseInt(statline[0]);
        
        StringBuilder newname = new StringBuilder();
                
        int healthDividerPos = 1;
        while(!statline[healthDividerPos].equals("|"))
        {
            newname.append(statline[healthDividerPos]).append(" ");
            healthDividerPos++;
        }
        int newCurrentHealth = Integer.parseInt(statline[healthDividerPos + 1]);
        int newMaximumHealth = Integer.parseInt(statline[healthDividerPos + 2]);
        
        Combatant newfighter = new Combatant (newinit, newname.toString(), newCurrentHealth, newMaximumHealth);
        brawl.fighters[brawl.totalfighters] = newfighter;
        brawl.totalfighters++;
        
        brawl = sortInitiative(brawl);
        return brawl;        
    }
    
    public static CombatArray damageFighter(CombatArray brawl)
    {
        int characterFound = findFighter(brawl, "");
        Scanner input = new Scanner(System.in);
        System.out.print("How much damage does " + brawl.fighters[characterFound].name + " take? ");
        int damageTaken = Integer.parseInt(input.nextLine());
        brawl.fighters[characterFound].currenthealth = brawl.fighters[characterFound].currenthealth - damageTaken;
        
        return brawl;
    }
    
    public static CombatArray healFighter(CombatArray brawl)
    {
        Scanner input = new Scanner(System.in);
        int characterFound = findFighter(brawl, "");
        if (characterFound != -1)
        {
            System.out.print("How much healing does " + brawl.fighters[characterFound].name + " get? ");
            int damageHealed = Integer.parseInt(input.nextLine());
            brawl.fighters[characterFound].currenthealth = brawl.fighters[characterFound].currenthealth + damageHealed;
            if (brawl.fighters[characterFound].currenthealth > brawl.fighters[characterFound].maxhealth)
            {
                brawl.fighters[characterFound].currenthealth = brawl.fighters[characterFound].maxhealth;
            }
        }

        return brawl;
    }
    
    public static int findFighter(CombatArray brawl, String characterName)
    {
        Scanner input = new Scanner(System.in);
        int characterFound = -1;
        int errorcount = 0;
        while (characterFound == -1)
        {
            if (characterName.equals("")){
                System.out.print("Which fighter is being targeted? ");
                characterName = input.nextLine();
            }

            for (int i = 0; i < brawl.totalfighters; i++)
            {
                if (brawl.fighters[i].name.equals(characterName))
                {
                    characterFound = i;
                    i = brawl.totalfighters;
                }
            }
            if (characterFound == -1)
            {
                System.out.println("There is no fighter with that name. Try again");
                errorcount++;
                characterName = "";
            }
            if (errorcount > 2)
            {
                System.out.println("You have mistyped too many times, taking you back to the main menu");
                return -1;
            }
        }
        return characterFound;
    }
    
    public static void savePartyStats(CombatArray brawl, String partyFileName) throws IOException
    {
       BufferedWriter writer = new BufferedWriter(new FileWriter(partyFileName)); 
       try 
       {
           for (int i = 0; i < brawl.totalpartymembers; i++)
           {
               int partyMemberInfoIndex = findFighter(brawl, brawl.partylist[i]);
               if (partyMemberInfoIndex != -1)
               {
                    StringBuilder characterInfo = new StringBuilder();
                    characterInfo.append(brawl.fighters[partyMemberInfoIndex].name);
                    characterInfo.append(" | ");
                    characterInfo.append(brawl.fighters[partyMemberInfoIndex].currenthealth);
                    characterInfo.append(" ");
                    characterInfo.append(brawl.fighters[partyMemberInfoIndex].maxhealth);
                    System.out.println(characterInfo);
                    writer.write(characterInfo.toString());
                    writer.newLine();
               }
           }  
       } 
       finally    
       {
           if (writer != null)
           {
               writer.close();
           }
       }
    }
}

