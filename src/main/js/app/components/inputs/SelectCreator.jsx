import React from 'react';
import styled from 'styled-components';
import { Button, Form, Input, Icon } from 'semantic-ui-react';
import { MULTIPLE_SELECT } from 'app/lib/messageTypes';
import TextInput from './TextInput';

const SelectItem = styled.span`
    display: flex;
    justify-content: space-between;
    padding: 0 5px;
    margin: 10px 0 10px 10px;
    background-color: #c8c8c8;
    border-color: #c8c8c8;
    background-image: none;
    color: rgba(0, 0, 0, 0.95);
    border-radius: 3px;
`;

const ListContainer = styled.div`
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    padding-top: 30px;
`;

const SelectedOptionsList = ({ options, removeFromList }) => (
    <ListContainer>
        {options.map(item => (
            <SelectItem key={item.id}>
                {item.text}
                <Icon
                    name="close"
                    onClick={removeFromList.bind(this, item.id)}
                />
            </SelectItem>
        ))}
    </ListContainer>
);

export default class SingleSelectInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            choicesList: [],
            optionName: ''
        };
        this.changeHandler = this.changeHandler.bind(this);
        this.createNewOption = this.createNewOption.bind(this);
        this.setOptionName = this.setOptionName.bind(this);
        this.removeFromList = this.removeFromList.bind(this);
    }

    createChoicesList() {
        return this.state.choicesList.map(item => {
            if (this.props.selectType === MULTIPLE_SELECT) {
                return {
                    selected: false,
                    text: item.text
                };
            } else {
                return {
                    type: 'selection',
                    text: item.text,
                    selected: false
                };
            }
        });
    }

    changeHandler() {
        const optionsList = this.createChoicesList();
        this.props.changeHandler(this.props.selectType, null, {
            value: optionsList
        });
    }

    setOptionName(e, { value }) {
        e.stopPropagation();
        e.preventDefault();
        this.setState({ optionName: value });
    }

    createNewOption(e) {
        e.stopPropagation();
        e.preventDefault();
        const { choicesList, optionName } = this.state;
        if (optionName) {
            this.setState(
                {
                    choicesList: [
                        ...choicesList,
                        {
                            id: Math.floor(Math.random() * 1000),
                            text: optionName
                        }
                    ],
                    optionName: ''
                },
                () => this.changeHandler()
            );
        }
    }

    removeFromList(id) {
        const { choicesList } = this.state;
        const newList = choicesList.filter(item => item.id !== id);
        this.setState({
            choicesList: newList
        });
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.cleanupForm) {
            this.setState({ choicesList: [] });
        }
    }

    render() {
        const { changeHandler, cleanupForm } = this.props;
        return (
            <React.Fragment>
                <TextInput
                    changeHandler={changeHandler}
                    cleanupForm={cleanupForm}
                />
                <Form.Field>
                    <label>Option text</label>
                    <Input
                        onChange={this.setOptionName}
                        style={{ width: '200px', marginRight: '10px' }}
                        value={this.state.optionName}
                    />
                    <Button onClick={this.createNewOption}>Create</Button>
                    <SelectedOptionsList
                        options={this.state.choicesList}
                        removeFromList={this.removeFromList}
                    />
                </Form.Field>
            </React.Fragment>
        );
    }
}
