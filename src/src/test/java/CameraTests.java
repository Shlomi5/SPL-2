package test.java;

import main.java.bgu.spl.mics.application.objects.Camera;
import main.java.bgu.spl.mics.application.objects.DetectedObject;
import main.java.bgu.spl.mics.application.objects.StampedDetectedObjects;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class CameraTests {

    Camera camera;


    public void setUp(int frequency, String cameraKey) {
        camera = new Camera(new AtomicInteger(1), new AtomicInteger(frequency), cameraKey);
        camera.loadDataBase("src/test/JsonResources/cameraResources/CameraTestDataBase.json");
    }

    @Test
    public void testStampedDetectedObjectsNoError() {
        //Arrange
        setUp(5, "camera1");

        //Act
        StampedDetectedObjects output = camera.checkAndDetectObjects(5); // Should capture 3 objects at times 2,4

        //Assert
        assertEquals(3,output.getDetectedObjects().size()); // check size
        assertEquals(5,output.getTimestamp()); // check time

        List<DetectedObject> realOutput = new CopyOnWriteArrayList<>();
        realOutput.add(new DetectedObject("Wall_1", "Wall"));
        realOutput.add(new DetectedObject("Wall_3", "Wall"));
        realOutput.add(new DetectedObject("Chair_Base_1", "Chair Base"));

        List<DetectedObject> outputList = output.getDetectedObjects();

        for (int i = 0; i < outputList.size(); i++) {
            assertEquals(realOutput.get(i).getId(), outputList.get(i).getId());
            assertEquals(realOutput.get(i).getDescription(), outputList.get(i).getDescription());
        }



    }

    @Test
    public void testStampedDetectedObjectsError() {
        //Arrange
        setUp(5, "camera2");

        //Act
        StampedDetectedObjects output = camera.checkAndDetectObjects(5); // Should capture 0 objects
        //Assert
        assert camera.crashed();
    }

}