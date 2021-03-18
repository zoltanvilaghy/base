package hu.bme.mit.train.sensor;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TrainSensorTest {
    TrainUser mockTU;
    TrainController mockTC;
    TrainSensorImpl ts;

    @Before
    public void init() {
        mockTU = mock(TrainUser.class);
        mockTC = mock(TrainController.class);
        ts = new TrainSensorImpl(mockTC, mockTU);
    }

    @Test
    public void NoAlarmWhenSpeedSetToZero() {
        ts.overrideSpeedLimit(0);
        when(mockTU.getAlarmState()).thenReturn(false);
        boolean alarmstate = mockTU.getAlarmState();
        verify(mockTU, times(1)).getAlarmState();
    }

    @Test
    public void AlarmWhenSpeedSetToNegative() {
        ts.overrideSpeedLimit(-1);
        when(mockTU.getAlarmState()).thenReturn(true);
        boolean alarmstate = mockTU.getAlarmState();
        verify(mockTU, times(1)).getAlarmState();
    }

    @Test
    public void AlarmWhenSpeedSetOver500() {
        ts.overrideSpeedLimit(501);
        when(mockTU.getAlarmState()).thenReturn(true);
        boolean alarmstate = mockTU.getAlarmState();
        verify(mockTU, times(1)).getAlarmState();
    }

    @Test
    public void AlarmWhenSpeedSetTooLowRelatively() {
        mockTC.setSpeedLimit(10);
        verify(mockTC, times(1)).setSpeedLimit(10);

        mockTC.setJoystickPosition(5);
        verify(mockTC, times(1)).setJoystickPosition(5);

        mockTC.followSpeed();
        when(mockTC.getReferenceSpeed()).thenReturn(5);
        mockTC.followSpeed();
        when(mockTC.getReferenceSpeed()).thenReturn(10);
        verify(mockTC, times(2)).followSpeed();

        ts.overrideSpeedLimit(1);
        ts.getSpeedLimit();

        when(mockTU.getAlarmState()).thenReturn(true);
        boolean alarmstate = mockTU.getAlarmState();
        verify(mockTU, times(1)).getAlarmState();
    }
}
