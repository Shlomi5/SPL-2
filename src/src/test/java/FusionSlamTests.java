package test.java;

import main.java.bgu.spl.mics.application.objects.CloudPoint;
import main.java.bgu.spl.mics.application.objects.Pose;
import main.java.bgu.spl.mics.application.objects.TrackedObject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static main.java.bgu.spl.mics.application.objects.FusionSlam.transformToGlobalCoordinates;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FusionSlamTests {

    @Test
    public void testTransformToGlobalCoordinatesBasic() {
        // Prepare input data
        Pose pose = new Pose(1, 3.0, 4.0, 90); // Robot at (3, 4) facing 90 degrees
        List<CloudPoint> localCloudPoints = Arrays.asList(
                new CloudPoint(1.0, 0.0),
                new CloudPoint(0.0, 1.0)
        );
        TrackedObject trackedObject = new TrackedObject("object1", 1, "TestObject", localCloudPoints);

        // Call the function
        List<CloudPoint> globalPoints = transformToGlobalCoordinates(pose, trackedObject);

        // Expected results
        assertEquals(2, globalPoints.size());
        assertEquals(3.0, globalPoints.get(0).getX(), 1e-6); // Rotated (1,0) -> (0,1) + Pose -> (3,5)
        assertEquals(5.0, globalPoints.get(0).getY(), 1e-6);
        assertEquals(2.0, globalPoints.get(1).getX(), 1e-6); // Rotated (0,1) -> (-1,0) + Pose -> (2,4)
        assertEquals(4.0, globalPoints.get(1).getY(), 1e-6);
    }

    @Test
    public void testTransformToGlobalCoordinatesEmptyCloudPoints() {
        // Prepare input data
        Pose pose = new Pose(2, 5.0, -3.0, 45); // Robot at (5, -3) facing 45 degrees
        List<CloudPoint> localCloudPoints = Collections.emptyList(); // No points
        TrackedObject trackedObject = new TrackedObject("object2", 2, "EmptyObject", localCloudPoints);

        // Call the function
        List<CloudPoint> globalPoints = transformToGlobalCoordinates(pose, trackedObject);

        // Verify the result
        assertEquals(0, globalPoints.size());
    }
}
