/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BlasterSubsystem;
import frc.robot.subsystems.LidarSubsystem;

public class BlasterDistanceBasedCommand extends CommandBase {
  /**
   * Creates a new BlasterDistanceBasedCommand.
   */
  BlasterSubsystem blasterSubsystem;
  private LidarSubsystem lidarSubsystem;

  public BlasterDistanceBasedCommand(BlasterSubsystem blasterSubsystem, LidarSubsystem lidarSubsystem) {
    this.blasterSubsystem = blasterSubsystem;
    this.lidarSubsystem = lidarSubsystem;

    SmartDashboard.putNumber("Blaster Velocity", blasterSubsystem.getVelocity());
    SmartDashboard.putNumber("Distance To Target", lidarSubsystem.getInches());

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(blasterSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putNumber("fps", 59);

    blasterSubsystem.setVelocity(10343);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // double distanceInFeet = lidarSubsystem.getInches() / 12;
    double distanceInFeet = 182/12;

    double feetsPerSec = (-0.0047360 * Math.pow(distanceInFeet, 3)) + (0.3441226 * Math.pow(distanceInFeet, 2))
        - (8.135303 * distanceInFeet) + 93.69801;

    feetsPerSec = feetsPerSec * 1.75;

    feetsPerSec = SmartDashboard.getNumber("fps", 0);

    double inchesInFeet = 12;
    double secondsInMinute = 60;
    double numTicksPer100ms = 2048;
    double flyWheelDiameterInInches = 8; // inches

    double adjustFor100ms = 600;

    double targetVelocity = (inchesInFeet * secondsInMinute * feetsPerSec * numTicksPer100ms)
        / (flyWheelDiameterInInches * adjustFor100ms * Math.PI);

    blasterSubsystem.setVelocity(targetVelocity);
    SmartDashboard.putNumber("Blaster Velocity", blasterSubsystem.getVelocity());
    double rpm = (blasterSubsystem.getVelocity() * adjustFor100ms) / numTicksPer100ms;
    double vfps = (rpm/secondsInMinute) * Math.PI * (flyWheelDiameterInInches/inchesInFeet);
    SmartDashboard.putNumber("Blaster Velocity (RPM)", rpm);
    SmartDashboard.putNumber("Blaster Velocity (Feets Per Sec)", vfps);
    SmartDashboard.putNumber("Distance To Target", lidarSubsystem.getInches());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    blasterSubsystem.output(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
