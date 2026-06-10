package org.tellervo.desktop.hardware;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.tellervo.desktop.hardware.AbstractMeasuringDevice.DataDirection;
import org.tellervo.desktop.hardware.device.GenericASCIIDevice;

public class AbstractMeasuringDeviceTest extends TestCase {

	public void testReceiverNotificationsRunOnEventDispatchThreadInOrder() throws Exception {
		System.setProperty("java.awt.headless", "true");

		final AbstractMeasuringDevice device = new GenericASCIIDevice();
		final RecordingReceiver receiver = new RecordingReceiver(2);
		device.setMeasurementReceiver(receiver);

		Thread serialThread = new Thread(new Runnable() {
			@Override
			public void run() {
				device.measuringSampleIONotify(new MeasuringSampleIOEvent(
						device, MeasuringSampleIOEvent.NEW_SAMPLE_EVENT, Integer.valueOf(12)));
				device.measuringSampleIONotify(new MeasuringSampleIOEvent(
						device, MeasuringSampleIOEvent.NEW_SAMPLE_EVENT, Integer.valueOf(34)));
			}
		});
		serialThread.start();
		serialThread.join();

		assertTrue("Timed out waiting for measurement notifications",
				receiver.awaitNotifications());
		assertTrue(receiver.notificationsRanOnEdt);
		assertEquals(Integer.valueOf(12), receiver.measurements.get(0));
		assertEquals(Integer.valueOf(34), receiver.measurements.get(1));
	}

	private static class RecordingReceiver implements MeasurementReceiver {
		private final CountDownLatch notifications;
		private final List<Integer> measurements = new ArrayList<Integer>();
		private boolean notificationsRanOnEdt = true;

		RecordingReceiver(int notificationCount) {
			notifications = new CountDownLatch(notificationCount);
		}

		boolean awaitNotifications() throws InterruptedException {
			return notifications.await(5, TimeUnit.SECONDS);
		}

		private void recordThread() {
			notificationsRanOnEdt &= SwingUtilities.isEventDispatchThread();
		}

		@Override
		public void receiverUpdateStatus(String status) {
			recordThread();
		}

		@Override
		public void receiverNewMeasurement(Integer value) {
			recordThread();
			measurements.add(value);
			notifications.countDown();
		}

		@Override
		public void receiverUpdateCurrentValue(Integer value) {
			recordThread();
		}

		@Override
		public void receiverRawData(DataDirection dir, String value) {
			recordThread();
		}
	}

}
