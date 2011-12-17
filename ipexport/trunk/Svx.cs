using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace IPExport
{
    /// <summary>
    ///  This is the base model class holding meta-data for an export.
    /// </summary>
    class Svx
    {
        private Event event_ = null;
        private List<Clip> clips = new List<Clip>();

        public Svx()
        {
        }

        public Svx(Event event_, List<Clip> clips)
        {
            this.event_ = event_;
            this.clips = clips;
        }

        public void addClip(Clip clip)
        {
            this.clips.Add(clip);
        }

        public Event Event
        {
            get { return event_; }
            set { this.event_ = value; }
        }

        public List<Clip> Clips
        {
            get { return clips; }
            set { this.clips = value; }
        }
    }
}
