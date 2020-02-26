/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import static frc.robot.Constants.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//make deploy intake, retract intake= put on smartdashboard

public class Intake extends SubsystemBase {
  public enum DeployState {
		DEPLOY, STOW, DEPLOYING
	}
	
  private DoubleSolenoid intakePistons;
  private TalonSRX intakeMotor, conveyorMotor, indexer;
  private DeployState intakeDeploymentState;
  private DigitalInput m_TopBeam, m_MiddleTopBeam, m_MiddleBottomBeam, m_BottomBeam, m_SendBeam;
  private boolean m_intakeup;

  /**
   * Creates a new Intake.
   */
  public Intake(){
    // Initialize Member Variables
    intakePistons = new DoubleSolenoid(INTAKE_SOLENOID_A, INTAKE_SOLENOID_B);

    intakeMotor = new TalonSRX(INTAKE); 
    intakeMotor.configFactoryDefault();
    conveyorMotor = new TalonSRX(INTAKE_CONVEYOR);
    conveyorMotor.configFactoryDefault();
    indexer = new TalonSRX(INTAKE_INDEXER);
    indexer.configFactoryDefault();

    m_SendBeam = new DigitalInput(BEAM_BREAKER_SEND);
    m_TopBeam = new DigitalInput(BEAM_BREAKER_RECEIVER_TOP);
    m_MiddleTopBeam = new DigitalInput(BEAM_BREAKER_RECEIVER_MIDDLETOP);
    m_MiddleBottomBeam = new DigitalInput(BEAM_BREAKER_RECEIVER_MIDDLEBOTTOM);
    m_BottomBeam = new DigitalInput(BEAM_BREAKER_RECEIVER_BOTTOM);

    Shuffleboard.getTab("Intake").add("Solenoid", intakePistons);
    Shuffleboard.getTab("Intake").add("Top", m_TopBeam);
    Shuffleboard.getTab("Intake").add("Middle Top", m_MiddleTopBeam);
    Shuffleboard.getTab("Intake").add("Middle Bottom", m_MiddleBottomBeam);
    Shuffleboard.getTab("Intake").add("Bottom Beam", m_BottomBeam);
    Shuffleboard.getTab("Intake").add("Send", m_SendBeam);
    Shuffleboard.getTab("Intake").add("Intake UP", m_intakeup);
 
    intakePistons.set(INTAKE_UP_POSITION);
    intakeDeploymentState = DeployState.STOW;
  }
  
  // Public method for commands to start the motors in reverse to eject balls
  public void EjectBalls(){
    intakeMotor.set(ControlMode.PercentOutput, -.5);
  }
  
  public void pullIntakeUp(){
    intakePistons.set(INTAKE_UP_POSITION);
    intakeDeploymentState = DeployState.STOW;
  }
  public void putIntakeDown() {
    intakePistons.set(INTAKE_DOWN_POSITION);
    intakeDeploymentState = DeployState.DEPLOY;
  }  

  public void pullInBalls(){
    

    // TODO check whether beam breaks are true or false when something is detected
    if( m_TopBeam.get() ){
      conveyorMotor.set(ControlMode.PercentOutput, CONVEYOR_SPEED);
    }
    else{
      conveyorMotor.set(ControlMode.PercentOutput, 0);
    }

    // Count how many beam breakers are detecting balls.  If all four of 
    // them  are full, we should probably stop the intake motors from
    // pulling more balls in as we'll likely just end up with balls stuck 
    // in the intake.
    int beamCount = 0;
    if( !m_TopBeam.get() ){
      beamCount++;
    }
    if( !m_MiddleTopBeam.get() ){
      beamCount++;
    }
    if(! m_MiddleBottomBeam.get() ){
      beamCount++;
    }
    if( !m_BottomBeam.get() ){
      beamCount++;
    }

    if( beamCount < 4 ){
      // This means that not all the beams are broken, and the top already checked that
      // we don't have a ball in there, so keep the conveyor, indexer, and intake going.
      intakeMotor.set(ControlMode.PercentOutput, INTAKE_SPEED);
      indexer.set(ControlMode.PercentOutput, INDEXER_SPEED);
    } else{
      stopMotors();
    }
  }

  public void shootBalls() {
    intakeMotor.set(ControlMode.PercentOutput, INTAKE_SPEED);
    indexer.set(ControlMode.PercentOutput, INDEXER_SPEED);
    conveyorMotor.set(ControlMode.PercentOutput, CONVEYOR_SPEED);

  }

  public void stopMotors(){
    indexer.set(ControlMode.PercentOutput, 0);
    intakeMotor.set(ControlMode.PercentOutput, 0);
    conveyorMotor.set(ControlMode.PercentOutput, 0);
  }

  public DeployState getIntakeState(){
    return intakeDeploymentState;
  }
}




