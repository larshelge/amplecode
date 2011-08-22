using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace IPExport
{
    /// <summary>
    ///  This is a model class holding information about a Clip, which loosely corresponds to a row in the MatchAnalysis table.
    /// </summary>
    class Clip
    {
        private int offset;
        private string team;
        private string filename;
        private string event_;
        private List<string> categories = new List<string>();
        private List<string> persons = new List<string>();

        public Clip()
        {
        }

        public Clip(int offset, string team, string filename, string event_)
        {
            this.offset = offset;
            this.team = team;
            this.filename = filename;
            this.event_ = event_;
        }

        public void addCategory(string category)
        {
            if (!string.IsNullOrWhiteSpace(category))
            {
                this.categories.Add(category);
            }
        }

        public void addPerson(string person)
        {
            if (!string.IsNullOrWhiteSpace(person))
            {
                this.persons.Add(person);
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

        public List<string> Persons
        {
            get { return persons; }
            set { this.persons = value; }
        }
    }
}
