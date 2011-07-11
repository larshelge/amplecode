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
        private List<Event> events = new List<Event>();
        private List<Clip> clips = new List<Clip>();

        public Svx()
        {
        }

        public Svx(List<Event> events, List<Clip> clips)
        {
            this.events = events;
            this.clips = clips;
        }

        public void addEvent(Event event_)
        {
            this.events.Add(event_);
        }

        public void addClip(Clip clip)
        {
            this.clips.Add(clip);
        }

        public List<Event> Events
        {
            get { return events; }
            set { this.events = value; }
        }

        public List<Clip> Clips
        {
            get { return clips; }
            set { this.clips = value; }
        }
    }
}
