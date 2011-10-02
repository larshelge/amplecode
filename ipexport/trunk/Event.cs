using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace IPExport
{
    /// <summary>
    ///  This is a model class holding information about an Event, which loosely corresponds to a row in the MatchInfo table.
    /// </summary>
    class Event
    {
        private string date;
        private string location;
        private string homeTeam;
        private string awayTeam;

        public Event()
        {
        }

        public Event(string date, string location, string homeTeam, string awayTeam)
        {
            this.date = date;
            this.location = location;
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
        }

        public string Date
        {
            get { return date; }
            set { this.date = value; }
        }

        public string Location
        {
            get { return location; }
            set { this.location = value; }
        }

        public string HomeTeam
        {
            get { return homeTeam; }
            set { this.homeTeam = value; }
        }

        public string AwayTeam
        {
            get { return awayTeam; }
            set { this.awayTeam = value; }
        }
    }
}
