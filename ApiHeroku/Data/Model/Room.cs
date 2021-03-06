using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ApiHeroku.Data.Model
{
    public class Room
    {
        [Required]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int ID { get; set; }
        public int Price { get; set; }
        public string Currency { get; set; }
        public int Capacity { get; set; }

        public int AccommodationId { get; set; }
        public Accommodation Accommodation { get; set; }

        //many-to-many
        public ICollection<Booking> Bookings { get; set; }
    }
}
