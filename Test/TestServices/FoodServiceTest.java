package TestServices;

import org.junit.*;
import service.FoodService;
import service.ServiceRequest;
import service.Staff;

import static org.junit.Assert.assertTrue;

public class FoodServiceTest {
    private FoodService foodService;
    private Staff John;
    private Staff Jane;
    private ServiceRequest foodRequest;

    @Before
    public void setup () {
        foodService = new FoodService();
        John = new Staff("johndoe", "abc", "Head cook", "John Doe", 123, foodService);
        Jane = new Staff("janedoe", "efg", "cook", "Jane Doe", 456, foodService);
        foodRequest = new ServiceRequest(foodService, 1);

        foodService.assignPerson(John);
        foodService.assignPerson(Jane);
    }


    @Test
    public void testFoodService() {
        assertTrue(foodService.getType().equals("Food Service"));
    }

    @Test
    public void testAssignPerson(){
        assertTrue(foodService.getPersonnel().contains(John));
    }

    @Test
    public void testFoodRequest(){
        assertTrue(foodRequest.getRequestID() == 1);
    }

    /*
     will need to write this eventually, but need to talk to map people first - Erika

    @Test
    public void testRequestService(){

    }
    */
}
