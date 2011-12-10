using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.OleDb;

namespace IPExport
{
    /// <summary>
    ///  This class is responsible for creating a SVX instance based on data retrieved from the database. 
    ///  
    ///  It is required that three additional tables are present and populated:
    ///  
    ///  == GroupVariablesCode (VariableGroup, VariablePosition, VariableCode) ==
    ///  VariableGroup: Refers to columns in Variables table, 1 = Starter, 2 = AttackType, 3 = FreeVar, 4 = Chances, 5 = GradeShort. 0 refers to Variables2 table.
    ///  VariablePosition: Refers to number in respective variable group, eg. FreeVar2 has position 2. VariableGroup 0 refers to RecIndex column in Variables2 table.
    ///  VariableCode: Refers to the NF unique code.
    ///  
    ///  == PlayerCode (PlayerIndex, PlayerCode) ==
    ///  PlayerIndex: Refers to PlayerIndex column in PlayerArchive table.
    ///  PlayerCode: Refers to the NF unique code.
    ///  
    ///  == TeamCode (TeamIndex, TeamCode) ==
    ///  TeamIndex: Refers to TeamIndex column in TeamArchive table.
    ///  TeamCode: Refers to the NF unique code.
    /// </summary>
    class SvxProvider
    {
        public Svx provide()
        {
            Svx svx = new Svx();

            CommandWrapper cw = new CommandWrapper().init();

            // Loading Events / MatchInfo

            string eventSql =
                "select MatchInfo.Date as date_, MatchInfo.Place as location, TC1.TeamCode as homeTeam, TC2.TeamCode as awayTeam " +
                "from (MatchInfo left join TeamCode TC1 on MatchInfo.HomeTeamNumb=TC1.TeamIndex) " +
                "left join TeamCode TC2 on MatchInfo.AwayTeamNumb=TC2.TeamIndex";

            OleDbDataReader eventReader = cw.getReader(eventSql);
            
            while (eventReader.Read())
            {
                Event event_ = new Event();
                
                event_.Date = eventReader["date_"] != DBNull.Value ? Convert.ToDateTime(eventReader["date_"]).ToString(ExportConstants.DATE_FORMAT) : "";
                event_.Location = Convert.ToString(eventReader["location"]);
                event_.HomeTeam = Convert.ToString(eventReader["homeTeam"]);
                event_.AwayTeam = Convert.ToString(eventReader["awayTeam"]);

                svx.addEvent(event_);
            }

            // Loading Clips / MatchAnalyse

            // Using modulus on the values in the variable columns in MatchAnalysis to remove insignificant digits as only the last is relevant

            string clipSql =
                "select MatchAnalyse.RecordFrameStart as start, TeamCode.TeamCode as team, MatchAnalyse.VideoFileName as filename, " +
                "PlayerCode.PlayerCode as person, MatchAnalyse.MatchIndex as event, " +

                "(select GroupVariablesCode.VariableCode from GroupVariablesCode " +
                "where GroupVariablesCode.VariableGroup=1 and (MatchAnalyse.PlayStart mod 10)=GroupVariablesCode.VariablePosition) as playStart, " +

                "(select GroupVariablesCode.VariableCode from GroupVariablesCode " +
                "where GroupVariablesCode.VariableGroup=3 and (MatchAnalyse.MVar1 mod 10)=GroupVariablesCode.VariablePosition) as freeVar, " +

                "(select GroupVariablesCode.VariableCode from GroupVariablesCode " +
                "where GroupVariablesCode.VariableGroup=0 and MatchAnalyse.MVar2=GroupVariablesCode.VariablePosition) as freeVar2, " +

                "(select GroupVariablesCode.VariableCode from GroupVariablesCode " +
                "where GroupVariablesCode.VariableGroup=4 and (MatchAnalyse.Chance mod 10)=GroupVariablesCode.VariablePosition) as chance, " +

                "(select GroupVariablesCode.VariableCode from GroupVariablesCode " +
                "where GroupVariablesCode.VariableGroup=5 and (MatchAnalyse.Grade mod 10)=GroupVariablesCode.VariablePosition) as grade " +

                "from (MatchAnalyse left join TeamCode on MatchAnalyse.TeamIndex=TeamCode.TeamIndex) " +
                "left join PlayerCode on MatchAnalyse.PlayerIndexPosFrom=PlayerCode.PlayerIndex";

            OleDbDataReader clipReader = cw.getReader(clipSql);

            while (clipReader.Read())
            {
                Clip clip = new Clip();

                clip.Start = Math.Abs(Convert.ToInt32(clipReader["start"] != null ? clipReader["start"] : 0) / ExportConstants.FRAMES_PER_SEC);
                clip.Team = Convert.ToString(clipReader["team"]);
                clip.Filename = Convert.ToString(clipReader["filename"] != null ? clipReader["filename"] : "").Trim();
                clip.Event = Convert.ToString(clipReader["event"]);

                clip.addCategory(Convert.ToString(clipReader["playStart"]));
                clip.addCategory(Convert.ToString(clipReader["freeVar"]));
                clip.addCategory(Convert.ToString(clipReader["freeVar2"]));
                clip.addCategory(Convert.ToString(clipReader["chance"]));
                clip.addCategory(Convert.ToString(clipReader["grade"]));

                clip.addPerson(Convert.ToString(clipReader["person"]));

                svx.addClip(clip);
            }

            cw.close();

            return svx;
        }
    }
}
