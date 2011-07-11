using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace IPExport
{
    /// <summary>
    ///  This is model class holding information about a Clip, which loosely corresponds to a row in the MatchAnalysis table.
    /// </summary>
    class Clip
    {
        private int offset;
        private string team;
        private string filename;
        private string person;
        private string event_;
        private List<string> categories = new List<string>();

        public Clip()
        {
        }

        public Clip(int offset, string team, string filename, string person, string event_)
        {
            this.offset = offset;
            this.team = team;
            this.filename = filename;
            this.person = person;
            this.event_ = event_;
        }

        public void addCategory(string category)
        {
            if (!string.IsNullOrWhiteSpace(category))
            {
                this.categories.Add(category);
            }
        }

        public int Offset
        {
            get { return offset; }
            set { this.offset = value; }
        }

        public string Team
        {
            get { return team; }
            set { this.team = value; }
        }

        public string Filename
        {
            get { return filename; }
            set { this.filename = value; }
        }

        public string Person
        {
            get { return person; }
            set { this.person = value; }
        }

        public string Event
        {
            get { return event_; }
            set { this.event_ = value; }
        }

        public List<string> Categories
        {
            get { return categories; }
            set { this.categories = value; }
        }
    }
}
