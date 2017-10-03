package com.sedientos.osm2mongo;

import com.sedientos.data.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class PlaceItemProcessor implements ItemProcessor<NodeDTO, Place> {

    private static final Logger log = LoggerFactory.getLogger(PlaceItemProcessor.class);

    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n" +
            "\n" +
            "Expetenda tincidunt in sed, ex partem placerat sea, porro commodo ex eam. His putant aeterno interesset at. Usu ea mundi tincidunt, omnium virtute aliquando ius ex. Ea aperiri sententiae duo. Usu nullam dolorum quaestio ei, sit vidit facilisis ea. Per ne impedit iracundia neglegentur. Consetetur neglegentur eum ut, vis animal legimus inimicus id.\n" +
            "\n" +
            "His audiam deserunt in, eum ubique voluptatibus te. In reque dicta usu. Ne rebum dissentiet eam, vim omnis deseruisse id. Ullum deleniti vituperata at quo, insolens complectitur te eos, ea pri dico munere propriae. Vel ferri facilis ut, qui paulo ridens praesent ad. Possim alterum qui cu. Accusamus consulatu ius te, cu decore soleat appareat usu.\n" +
            "\n" +
            "Est ei erat mucius quaeque. Ei his quas phaedrum, efficiantur mediocritatem ne sed, hinc oratio blandit ei sed. Blandit gloriatur eam et. Brute noluisse per et, verear disputando neglegentur at quo. Sea quem legere ei, unum soluta ne duo. Ludus complectitur quo te, ut vide autem homero pro.\n" +
            "\n" +
            "Vis id minim dicant sensibus. Pri aliquip conclusionemque ad, ad malis evertitur torquatos his. Has ei solum harum reprimique, id illum saperet tractatos his. Ei omnis soleat antiopam quo. Ad augue inani postulant mel, mel ea qualisque forensibus.";

    @Override
    public Place process(NodeDTO node) throws Exception {
        log.debug("Processing node {0}", node);
        Place place = new Place();
        Point location = new Point(node.getLon().doubleValue(), node.getLat().doubleValue());
        place.setLocation(location);
        place.setOsmId(node.getId());
        place.setOsmVersion(node.getOsmVersion());
        for (Tag tag : node.getTags()) {
            String key = tag.getK();
            switch (key) {
                case "name":
                    place.setName(tag.getV());
                    break;
                case "addr:street":
                    place.setStreet(tag.getV());
                    break;
                case "addr:housenumber":
                    place.setHouseNumber(tag.getV());
                    break;
                case "addr:city":
                    place.setCity(tag.getV());
                    break;
                case "addr:postcode":
                    place.setPostalCode(tag.getV());
                    break;
                case "phone":
                case "contact:phone":
                    place.setPhoneNumber(tag.getV());
                    break;
                case "website":
                case "contact:facebook":
                    place.setWebsite(tag.getV());
                    break;
                case "email":
                case "contact:email":
                    place.setEmail(tag.getV());
                    break;
                case "opening_hours":
                    place.setOpeningHours(tag.getV());
                    break;
            }
        }
        if (place.getName() == null) {
            // If we don't even have the place name, we don't want to add it to the collection
            return null;
        }

        place.setAddress(getFullAddress(place));
        place.setBeers(generateRandomBeers());
        place.setReviews(generateRandomReviews());
        place.setStars(calculateStars(place.getReviews()));
        log.debug("Created place {0}", place);
        return place;
    }

    private String getFullAddress(Place place) {
        String street = place.getStreet();
        String address = null;
        if (street != null && !street.isEmpty()) {
            address = String.format("%s, %s %s %s",
                    getEmptyIfNull(street),
                    getEmptyIfNull(place.getHouseNumber()),
                    getEmptyIfNull(place.getPostalCode()),
                    getEmptyIfNull(place.getCity()));
            address = address.trim();
        }
        return address;
    }

    private String getEmptyIfNull(String s) {
        return s == null ? "" : s;
    }

    private List<Beer> generateRandomBeers() {
        List<Beer> beers = new ArrayList<>();
        Random r = new Random();

        beers.add(new Beer(GlassSize.SMALL, 1 + r.nextDouble()));
        if (r.nextDouble() > 0.3) {
            beers.add(new Beer(GlassSize.DOUBLE, 2 + r.nextDouble()));
        }
        if (r.nextDouble() > 0.8) {
            beers.add(new Beer(GlassSize.PINT, 4 + r.nextDouble()));
        }
        return beers;
    }

    private List<Review> generateRandomReviews() {
        List<Review> reviews = new ArrayList<>();
        Random r = new Random();
        Review review;
        User user;
        String[] words = LOREM_IPSUM.split(" ");
        Date firstDate = new Date();
        firstDate.setTime(1475490500);
        Date now = new Date();
        long firstTime = firstDate.getTime();
        long nowTime = now.getTime();
        if (r.nextBoolean()) {
            while (r.nextDouble() > 0.1) {
                review = new Review();
                review.setBody(String.join(" ", Arrays.copyOfRange(words, 0, r.nextInt(words.length))));
                int stars = r.nextInt(7);
                if (stars > 5) {
                    stars = 5;
                }
                review.setStars(stars);
                String userName = words[r.nextInt(words.length)];
                user = new User(userName, "email@world.org");
                review.setUser(user);
                Date randomDate = new Date(nowTime - firstTime + r.nextInt((int) firstTime));
                review.setDate(randomDate);
                reviews.add(review);
            }
        }
        return reviews;
    }

    private BigDecimal calculateStars(List<Review> reviews) {
        OptionalDouble average = reviews.stream()
//                .map(r -> r.getStars())
                .mapToInt(Review::getStars)
                .average();
        if (average.isPresent()) {
            return new BigDecimal(average.getAsDouble(), new MathContext(2));
        } else {
            return null;
        }
    }

}
