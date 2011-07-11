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
    ///  GroupVariablesCode (VariableGroup, VariablePosition, VariableCode)
    ///  PlayerCode (PlayerIndex, PlayerCode)
    ///  TeamCode (TeamIndex, TeamCode)
    /// </summary>
    class SvxProvider
    {
        public Svx provide()
        {
            Svx svx = new Svx();

            CommandWrapper cw = new CommandWrapper().init();

            // Loading Events / MatchInfo

            string eventSql =
                "select MatchInfo.MatchIndex as code, MatchInfo.Date as date_, MatchInfo.Place as location, TC1.TeamCode as homeTeam, TC2.TeamCode as awayTeam " +
                "from (MatchInfo left join TeamCode TC1 on MatchInfo.HomeTeamNumb=TC1.TeamIndex) " +
                "left join TeamCode TC2 on MatchInfo.AwayTeamNumb=TC2.TeamIndex";

            OleDbDataReader eventReader = cw.getReader(eventSql);
            
            while (eventReader.Read())
            {
                Event event_ = new Event();
                
                event_.Code = Convert.ToString(eventReader["code"]);
                event_.Date = eventReader["date_"] != DBNull.Value ? Convert.ToDateTime(eventReader["date_"]).ToString(ExportConstants.DATE_FORMAT) : "";
                event_.Location = Convert.ToString(eventReader["location"]);
                event_.HomeTeam = Convert.ToString(eventReader["homeTeam"]);
                event_.AwayTeam = Convert.ToString(eventReader["awayTeam"]);

                svx.addEvent(event_);
            }

            // Loading Clips / MatchAnalyse

            // Using modulus on the values in the variable columns in MatchAnalysis to remove insignificant digits as only the last is relevant

            string clipSql =
                "select MatchAnalyse.RecordFrameStart as offset, TeamCode.TeamCode as team, MatchAnalyse.VideoFileName as filename, " +
                "PlayerCode.PlayerCode as person, MatchAnalyse.MatchIndex as event, " +
                "(select GroupVariablesCode.VariableCode from GroupVariablesCode " +
                "where GroupVariablesCode.VariableGroup=1 and (MatchAnalyse.PlayStart mod 10)=GroupVariablesCode.VariablePosition) as playStart, " +
                "(select GroupVariablesCode.VariableCode from GroupVariablesCode " +
                "where GroupVariablesCode.VariableGroup=3 and (MatchAnalyse.MVar1 mod 10)=GroupVariablesCode.VariablePosition) as freeVar, " +
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

                clip.Offset = (Convert.ToInt32(clipReader["offset"] != null ? clipReader["offset"] : 0) / ExportConstants.FRAMES_PER_SEC);
                clip.Team = Convert.ToString(clipReader["team"]);
                clip.Filename = Convert.ToString(clipReader["filename"]);
                clip.Person = Convert.ToString(clipReader["person"]);
                clip.Event = Convert.ToString(clipReader["event"]);

                clip.addCategory(Convert.ToString(clipReader["playStart"]));
                clip.addCategory(Convert.ToString(clipReader["freeVar"]));
                clip.addCategory(Convert.ToString(clipReader["chance"]));
                clip.addCategory(Convert.ToString(clipReader["grade"]));

                svx.addClip(clip);
            }

            cw.close();

            return svx;
        }
    }
}
