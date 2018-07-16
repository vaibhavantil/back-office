import React from "react";
import PropTypes from "prop-types";
import { Header, Table, Button, Modal, Icon, Form } from "semantic-ui-react";
import TableFields from "components/shared/table-fields/TableFields";
import InsuranceTableRows from "../insurance-table-rows/InsuranceTableRows";
import { getFieldName, getFieldValue } from "app/lib/helpers";

export default class InsuranceTab extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modalOpen: false,
      insurance: [],
      availableSafetyIncreasers: {
        SMOKE_ALARM: "Smoke alarm",
        FIRE_EXTINGUISHER: "Fire extinguisher",
        SAFETY_DOOR: "Safety door",
        GATE: "Gate",
        BURGLAR_ALARM: "Burglar alarm",
        NONE: "None"
      },
      safetyIncreasers: [],
      isStudent: true
    };
  }

  handleOpen = () => this.setState({ modalOpen: true });

  handleClose = () => {
    this.setState({ modalOpen: false });
    this.setState({ insurance: [] });
    this.setState({ safetyIncreasers: [] });
  };

  shouldBeDisplayed = field => {
    switch (field.toLowerCase()) {
      case "personsinhousehold":
      case "insurancetype":
      case "street":
      case "city":
      case "zipcode":
      case "floor":
      case "livingspace":
        return true;
      default:
        return false;
    }
  };

  handleChange = field => e => {
    let { insurance, safetyIncreasers } = this.state;

    switch (field) {
      case "SMOKE_ALARM":
      case "FIRE_EXTINGUISHER":
      case "SAFETY_DOOR":
      case "GATE":
      case "BURGLAR_ALARM":
      case "NONE":
        safetyIncreasers.indexOf(field) === -1
          ? safetyIncreasers.push(field)
          : safetyIncreasers.splice(safetyIncreasers.indexOf(field), 1);
        insurance.safetyIncreasers = safetyIncreasers;
        break;
      case "isStudent":
        this.setState({ isStudent: !this.state.isStudent });
        insurance[field] = this.state.isStudent;
        break;
      default:
        insurance[field] = e.target.value;
        break;
    }
    this.setState({ insurance });
  };

  handleCancel = () => {
    this.handleClose();
  };

  handleSubmissionButton = () => {
    const { createModifiedInsurance, insurance } = this.props;
    let submittedInsurance = { ...insurance.data, ...this.state.insurance };
    createModifiedInsurance(insurance.data.memberId, submittedInsurance);
    this.handleClose();
  };

  render() {
    const {
      insurance: { data }
    } = this.props;
    let activeDate;
    let cancellationDate;
    let fields;

    if (data) {
      activeDate = activeDate ? activeDate : data.insuranceActiveFrom;
      cancellationDate = cancellationDate
        ? cancellationDate
        : data.insuranceActiveTo;
      /* eslint-disable no-unused-vars */
      const {
        insuranceActiveFrom,
        insuranceActiveTo,
        insuredAtOtherCompany,
        cancellationEmailSent,
        certificateUploaded,
        certificateUrl,
        ...filteredFields
      } = data;
      /* eslint-enable */

      fields = filteredFields;
    }

    return fields ? (
      <React.Fragment>
        <Table selectable>
          <Table.Body>
            <TableFields fields={fields} />
            <InsuranceTableRows
              activeDate={activeDate}
              cancellationDate={cancellationDate}
              fields={fields}
              {...this.props}
            />
          </Table.Body>
          <Table.Footer fullWidth>
            <Table.Row>
              <Table.HeaderCell />
              <Table.HeaderCell colSpan="2">
                <Modal
                  trigger={
                    <Button
                      floated="right"
                      icon
                      labelPosition="left"
                      primary
                      size="medium"
                      onClick={this.handleOpen}
                    >
                      <Icon name="edit" /> Create modified insurance
                    </Button>
                  }
                  open={this.state.modalOpen}
                  onClose={this.handleClose}
                  basic
                  size="small"
                  dimmer="blurring"
                >
                  <Header icon="edit" content="Modify Insurance" />
                  <Modal.Content>
                    <Form inverted size="small">
                      <React.Fragment>
                        {Object.keys(data).map(
                          (field, productId) =>
                            this.shouldBeDisplayed(field) ? (
                              <Form.Input
                                key={productId}
                                label={getFieldName(field)}
                                defaultValue={getFieldValue(data[field])}
                                onChange={this.handleChange(field)}
                              />
                            ) : (
                              ""
                            )
                        )}
                        <Form.Group inverted grouped>
                          <label>Safety Items</label>
                          {Object.keys(
                            this.state.availableSafetyIncreasers
                          ).map(field => (
                            <Form.Checkbox
                              key={field}
                              label={getFieldValue(
                                this.state.availableSafetyIncreasers[field]
                              )}
                              onChange={this.handleChange(field)}
                            />
                          ))}
                        </Form.Group>
                        <Form.Group inverted grouped>
                          <label>Student</label>
                          <Form.Checkbox
                            key="isStudent"
                            label="Is Student?"
                            onChange={this.handleChange("isStudent")}
                          />
                        </Form.Group>
                      </React.Fragment>
                      <Button.Group floated="right" labelPosition="left">
                        <Button type="button" onClick={this.handleCancel}>
                          Cancel
                        </Button>
                        <Button.Or />
                        <Button
                          type="button"
                          onClick={this.handleSubmissionButton}
                          positive
                        >
                          Submit
                        </Button>
                      </Button.Group>
                    </Form>
                  </Modal.Content>
                </Modal>
              </Table.HeaderCell>
            </Table.Row>
          </Table.Footer>
        </Table>
      </React.Fragment>
    ) : (
      <Header>No insurance info </Header>
    );
  }
}

InsuranceTab.propTypes = {
  insurance: PropTypes.object.isRequired,
  messages: PropTypes.object.isRequired,
  saveInsuranceDate: PropTypes.func.isRequired,
  sendCancelRequest: PropTypes.func.isRequired,
  createModifiedInsurance: PropTypes.func.isRequired
};
