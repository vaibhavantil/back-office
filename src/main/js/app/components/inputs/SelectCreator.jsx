import React from 'react';
import styled from 'styled-components';
import { Button, Form, Input, Icon } from 'semantic-ui-react';
import { TEXT } from 'app/lib/messageTypes';

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

    changeHandler() {
        this.props.changeHandler(this.props.selectType, null, {
            value: this.state.choicesList
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
        this.setState({
            choicesList: [
                ...choicesList,
                { id: Math.floor(Math.random() * 100), text: optionName }
            ],
            optionName: ''
        }, () => this.changeHandler());
    }

    removeFromList(id) {
        const { choicesList } = this.state;
        const newList = choicesList.filter(item => item.id !== id);
        this.setState({
            choicesList: newList
        });
    }

    render() {
        return (
            <React.Fragment>
                <Form.Field>
                    <label>Text</label>
                    <Input
                        fluid
                        onChange={this.props.changeHandler.bind(this, TEXT)}
                    />
                </Form.Field>
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
